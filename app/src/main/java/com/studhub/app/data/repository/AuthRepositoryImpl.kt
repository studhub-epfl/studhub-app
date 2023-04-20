package com.studhub.app.data.repository

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.Api
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.studhub.app.core.Constants.SIGN_IN_REQUEST
import com.studhub.app.core.Constants.SIGN_UP_REQUEST
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private val db: FirebaseDatabase
) : AuthRepository {
    private enum class AuthType {
        NONE,
        ONE_TAP,
        EMAIL
    }

    private var authType: AuthType = AuthType.NONE

    private val internalDb = db.getReference("users")

    override val isUserAuthenticatedInFirebase: Boolean
        get() = auth.currentUser != null

    override val currentUserUid: String
        get() = if (auth.currentUser != null) auth.currentUser!!.uid else ""

    override suspend fun oneTapSignInWithGoogle(): Flow<ApiResponse<BeginSignInResult>> = flow {
        try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            authType = AuthType.ONE_TAP
            emit(ApiResponse.Success(signInResult))
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                emit(ApiResponse.Success(signUpResult))
            } catch (e: Exception) {
                emit(ApiResponse.Failure(e.message.orEmpty()))
            }
        }
    }

    override suspend fun firebaseSignInWithGoogle(
        googleCredential: AuthCredential
    ): Flow<ApiResponse<Boolean>> = flow {
        try {
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false

            if (isNewUser) {
                addUserToFirebase()
            }

            emit(ApiResponse.Success(isNewUser))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message.orEmpty()))
        }
    }

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            addUserToFirebase()
            authType = AuthType.EMAIL
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
            emit(ApiResponse.Failure(e.message.orEmpty().ifEmpty { "Sending verification email failed" }))
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        try {
            auth.signInWithEmailAndPassword(email, password).await()
            authType = AuthType.EMAIL
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
            emit(ApiResponse.Failure(e.message.orEmpty().ifEmpty { "Sending reset password email failed" }))
        }
    }

    override suspend fun signOut(): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        try {
            if (authType == AuthType.ONE_TAP) {
                oneTapClient.signOut().await()
            }

            auth.signOut()
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message.orEmpty().ifEmpty { "Error while signing out" }))
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
