package com.studhub.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.core.utils.ApiResponse.Companion.NO_INTERNET_CONNECTION
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.network.NetworkStatus
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase,
    private val localDb: LocalDataSource,
    private val networkStatus: NetworkStatus
) : AuthRepository {
    private val internalDb = db.getReference("users")

    override val isUserAuthenticatedInFirebase: Boolean
        get() = auth.currentUser != null

    override val isEmailVerified: Boolean
        get() = auth.currentUser?.isEmailVerified ?: false

    override val currentUserUid: String
        get() = auth.currentUser?.uid ?: ""

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            emit(NO_INTERNET_CONNECTION)
            return@flow
        }

        if (!(email.endsWith("epfl.ch") || email.endsWith("unil.ch"))) {
            emit(ApiResponse.Failure("You must use an epfl.ch or unil.ch email address in order to register."))
            return@flow
        }

        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            addUserToFirebase()

            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message.orEmpty().ifEmpty { "User sign-up failed" }))
        }
    }

    override suspend fun sendEmailVerification(): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            emit(NO_INTERNET_CONNECTION)
            return@flow
        }

        try {
            if (auth.currentUser == null) {
                emit(ApiResponse.Failure("No user logged-in"))
                return@flow
            }

            auth.currentUser!!.sendEmailVerification().await()
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(
                ApiResponse.Failure(
                    e.message.orEmpty().ifEmpty { "Sending verification email failed" })
            )
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            emit(NO_INTERNET_CONNECTION)
            return@flow
        }

        try {
            auth.signInWithEmailAndPassword(email, password).await()
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message.orEmpty().ifEmpty { "Authentication failed" }))
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            emit(NO_INTERNET_CONNECTION)
            return@flow
        }

        try {
            auth.sendPasswordResetEmail(email).await()
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(
                ApiResponse.Failure(
                    e.message.orEmpty().ifEmpty { "Sending reset password email failed" })
            )
        }
    }

    override suspend fun signOut(): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        try {
            removeLoggedInUserFromCache()
            auth.signOut()
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message.orEmpty().ifEmpty { "Error while signing out" }))
        }
    }

    override suspend fun reloadUser(): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            emit(NO_INTERNET_CONNECTION)
            return@flow
        }

        try {
            auth.currentUser?.reload()?.await()
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message.orEmpty().ifEmpty { "Reloading user failed" }))
        }
    }

    override fun getAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = AuthStateListener {
            trySend(it.currentUser != null)
        }

        auth.addAuthStateListener(authStateListener)

        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    private fun addUserToFirebase() {
        auth.currentUser?.apply {
            val user: User = toUser()
            internalDb.child(uid).setValue(user)
        }
    }

    private suspend fun removeLoggedInUserFromCache() {
        localDb.removeUser(currentUserUid)
    }

}

fun FirebaseUser.toUser() = User(
    id = uid,
    userName = displayName.orEmpty().ifEmpty { "Anonymous" },
    email = email.orEmpty(),
    profilePicture = photoUrl.toString(),
    phoneNumber = phoneNumber.orEmpty()

)
