package com.studhub.app.domain.repository

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.studhub.app.core.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isUserAuthenticatedInFirebase: Boolean

    val currentUserUid: String

    /**
     * Start sign-in process with google one-tap sign-in
     */
    suspend fun oneTapSignInWithGoogle(): Flow<ApiResponse<BeginSignInResult>>

    /**
     * Authenticate to firebase with the given [googleCredential]
     * @return a [Flow] of [ApiResponse] with the last one being true iff the user is a new user
     */
    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Flow<ApiResponse<Boolean>>

    /**
     * Create an account with the given ([email], [password]) tuple and log-in
     * @return a [Flow] of [ApiResponse] with the last one being true iff sign-up completed successfully
     */
    suspend fun signUpWithEmailAndPassword(email: String, password: String): Flow<ApiResponse<Boolean>>


    /**
     * Send an email to verify TODO("something")
     * @return a [Flow] of [ApiResponse] with the last one being true on success
     */
    suspend fun sendEmailVerification(): Flow<ApiResponse<Boolean>>


    /**
     * Authenticate the user given the ([email], [password]]) tuple
     * @return a [Flow] of [ApiResponse] with the last one being true iff authentication succeeded
     */
    suspend fun signInWithEmailAndPassword(email: String, password: String): Flow<ApiResponse<Boolean>>


    /**
     * Send an email to reset the password to the user with given [email]
     * @return a [Flow] of [ApiResponse] with the last one being true on success
     */
    suspend fun sendPasswordResetEmail(email: String): Flow<ApiResponse<Boolean>>

    /**
     * Sign out of the application
     */
    suspend fun signOut(): Flow<ApiResponse<Boolean>>
}
