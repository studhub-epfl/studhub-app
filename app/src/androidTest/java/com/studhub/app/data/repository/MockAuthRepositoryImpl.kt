package com.studhub.app.data.repository

import android.app.PendingIntent
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@Singleton
class MockAuthRepositoryImpl: AuthRepository {
    private var isLoggedIn = false

    companion object {
        val loggedInUser = User(
            id = "wiufhb",
            userName = "Stud Hub",
            email = "stud.hub@studhub.ch")

    }

    override val isUserAuthenticatedInFirebase: Boolean
        get() = isLoggedIn

    override val currentUserUid: String
        get() = if (isLoggedIn) loggedInUser.id else ""

    override suspend fun oneTapSignInWithGoogle(): Flow<ApiResponse<BeginSignInResult>> {
        isLoggedIn = true
        return flowOf(ApiResponse.Loading)
    }

    override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Flow<ApiResponse<Boolean>> {
        isLoggedIn = true
        return flowOf(ApiResponse.Loading)
    }
}