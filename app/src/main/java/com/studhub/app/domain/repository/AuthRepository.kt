package com.studhub.app.domain.repository

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.studhub.app.core.utils.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val isUserAuthenticatedInFirebase: Boolean

    val isEmailVerified: Boolean

    val currentUserUid: String

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

    /**
     * Reloads data of the current user
     * @return a [Flow] of [ApiResponse] with the last one being true on success
     */
    suspend fun reloadUser(): Flow<ApiResponse<Boolean>>

    /**
     * Retrieves the current authentication status
     * @return true iff a user is currently logged-in
     */
    fun getAuthState(): Flow<Boolean>
}
