package com.studhub.app.presentation.auth

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.repository.MockAuthRepositoryImpl
import com.studhub.app.domain.repository.AuthRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthViewModelTest {
    private lateinit var authRepo: AuthRepository

    private lateinit var viewModel: AuthViewModel

    @Before
    fun init() {
        authRepo = MockAuthRepositoryImpl()
        viewModel = AuthViewModel(authRepo)
    }

    @Test
    fun getAuthStateHoldsCorrectValue() {
        runBlocking {
            launch {
                viewModel.getAuthState().collect { connectionStatus ->
                    assertTrue("Mock user should be logged in", connectionStatus)
                    cancel()
                }
            }
        }
    }

    @Test
    fun isUserAuthenticatedHoldsTheCorrectValue() {
        assertTrue("Mock user should be logged in", viewModel.isUserAuthenticated)

        val authRepoNotLoggedIn = MockAuthRepositoryImpl(false)
        val viewModelNotLoggedIn = AuthViewModel(authRepoNotLoggedIn)

        assertFalse("Mock user should not be logged in", viewModelNotLoggedIn.isUserAuthenticated)
    }

    @Test
    fun signOutAndSignInWorkCorrectly() {
        runBlocking { viewModel.signOut() }
        // wait for dispatch
        runBlocking { delay(50) }
        assertFalse(
            "Mock user should be logged out after sign out",
            authRepo.isUserAuthenticatedInFirebase
        )

        runBlocking { viewModel.signUpWithEmailAndPassword("dummy", "input") }
        // wait for dispatch
        runBlocking { delay(50) }
        assertTrue(
            "Mock user should be logged in after sign in",
            authRepo.isUserAuthenticatedInFirebase
        )
    }

    @Test
    fun signInDispatchesCorrectResponseValue() {
        runBlocking { viewModel.signInWithEmailAndPassword("dummy", "input") }
        // wait for dispatch
        runBlocking { delay(50) }

        assertEquals(
            "Dispatched response should contain true",
            ApiResponse.Success(true),
            viewModel.signInResponse
        )
    }

    @Test
    fun signUpDispatchesCorrectResponseValue() {
        runBlocking { viewModel.signUpWithEmailAndPassword("dummy", "input") }
        // wait for dispatch
        runBlocking { delay(50) }

        assertEquals(
            "Dispatched response should contain true",
            ApiResponse.Success(true),
            viewModel.signUpResponse
        )
    }

    @Test
    fun sendEmailVerificationDispatchesCorrectResponseValue() {
        runBlocking { viewModel.sendEmailVerification() }
        // wait for dispatch
        runBlocking { delay(50) }

        assertEquals(
            "Dispatched response should contain true",
            ApiResponse.Success(true),
            viewModel.sendEmailVerificationResponse
        )
    }

    @Test
    fun reloadUserDispatchesCorrectResponseValue() {
        runBlocking { viewModel.reloadUser() }
        // wait for dispatch
        runBlocking { delay(50) }

        assertEquals(
            "Dispatched response should contain true",
            ApiResponse.Success(true),
            viewModel.reloadUserResponse
        )
    }

    @Test
    fun sendPasswordResetEmailDispatchesCorrectResponseValue() {
        runBlocking { viewModel.sendPasswordResetEmail("email") }
        // wait for dispatch
        runBlocking { delay(50) }

        assertEquals(
            "Dispatched response should contain true",
            ApiResponse.Success(true),
            viewModel.sendPasswordResetEmailResponse
        )
    }
}
