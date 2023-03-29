package com.studhub.app.data.repository

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@Singleton
class MockAuthRepositoryImpl(isLoggedInByDefault: Boolean = true) : AuthRepository {
    private var isLoggedIn = isLoggedInByDefault

    companion object {
        val loggedInUser = User(
            id = "wiufhb",
            userName = "Stud Hub",
            email = "stud.hub@studhub.ch",
            phoneNumber = "0000000000"
        )

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

    override suspend fun signOut(): Flow<ApiResponse<Boolean>> {
        isLoggedIn = false
        return flowOf(ApiResponse.Success(true))
    }
}
