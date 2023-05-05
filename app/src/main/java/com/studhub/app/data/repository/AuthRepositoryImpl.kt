package com.studhub.app.data.repository

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.studhub.app.core.Constants.SIGN_IN_REQUEST
import com.studhub.app.core.Constants.SIGN_UP_REQUEST
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase
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

        try {
            auth.signInWithEmailAndPassword(email, password).await()
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message.orEmpty().ifEmpty { "Authentication failed" }))
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

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
            auth.signOut()
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message.orEmpty().ifEmpty { "Error while signing out" }))
        }
    }

    override suspend fun reloadUser(): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

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

}

fun FirebaseUser.toUser() = User(
    id = uid,
    userName = displayName.orEmpty().ifEmpty { "Anonymous" },
    email = email.orEmpty(),
    profilePicture = photoUrl.toString(),
    phoneNumber = phoneNumber.orEmpty()

)
