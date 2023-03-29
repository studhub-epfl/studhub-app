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
     */
    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Flow<ApiResponse<Boolean>>

    /**
     * Sign out of the application
     */
    suspend fun signOut(): Flow<ApiResponse<Boolean>>
}
