package com.studhub.app.domain.repository

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.studhub.app.core.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isUserAuthenticatedInFirebase: Boolean

    val currentUserUid: String

    suspend fun oneTapSignInWithGoogle(): Flow<ApiResponse<BeginSignInResult>>

    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Flow<ApiResponse<Boolean>>
}
