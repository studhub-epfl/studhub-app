package com.studhub.app.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.UserRepository
import com.studhub.app.domain.usecase.user.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.random.Random

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AuthRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var oneTapClient: SignInClient

    @Inject
    lateinit var signInRequest: BeginSignInRequest

    @Inject
    lateinit var signUpRequest: BeginSignInRequest

    @Inject
    lateinit var db: FirebaseDatabase

    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        hiltRule.inject()
        authRepository = AuthRepositoryImpl(auth, oneTapClient, signInRequest, signUpRequest, db)
    }

    @Test
    fun Test1() = runBlockingTest {
        // Arrange
        val signInResult = BeginSignInResult(mockk(relaxed = true))
        coEvery { oneTapClient.beginSignIn(signInRequest).await() } returns signInResult

        // Act
        val apiResponse = authRepository.oneTapSignInWithGoogle().first()

        // Assert
        assertEquals(ApiResponse.Success(signInResult), apiResponse)
    }

    @Test
    fun test2() = runBlockingTest {
        // Arrange
        val signUpResult = BeginSignInResult(mockk(relaxed = true))
        coEvery { oneTapClient.beginSignIn(signInRequest).await() } throws Exception()
        coEvery { oneTapClient.beginSignIn(signUpRequest).await() } returns signUpResult

        // Act
        val apiResponse = authRepository.oneTapSignInWithGoogle().first()

        // Assert
        assertEquals(ApiResponse.Success(signUpResult), apiResponse)
    }

    @Test
    fun test3() = runBlockingTest {
        // Arrange
        coEvery { oneTapClient.beginSignIn(signInRequest).await() } throws Exception()
        coEvery { oneTapClient.beginSignIn(signUpRequest).await() } throws Exception()

        // Act
        val apiResponse = authRepository.oneTapSignInWithGoogle().first()

        // Assert
        assert(apiResponse is ApiResponse.Failure)
    }
}



/*

    @Test
    fun `firebaseSignInWithGoogle() should return success if sign-in with credentials succeeds and user is new`() =
        runBlockingTest {
            // Arrange
            val authResult = mockk<FirebaseAuth>(relaxed = true)
            val googleSignInAccount = mockk<GoogleSignInAccount>()
            val googleSignInCredential = mockk<GoogleSignInCredential>()
            val firebaseUser = mockk<FirebaseUser>()
            coEvery { authResult.signInWithCredential(googleSignInCredential) } returns mockk {
                every { user } returns firebaseUser
                every { additionalUserInfo } returns mockk {
                    every { isNewUser } returns true
                }
            }

            // Act
            val result = firebaseAuthRepository.firebaseSignInWithGoogle(
                googleSignInAccount,
                googleSignInCredential
            )

            // Assert
            assertTrue(result is FirebaseResult.Success)
            verify { authResult.signInWithCredential(googleSignInCredential) }
        }

    @Test
    fun `firebaseSignInWithGoogle() should return success if sign-in with credentials succeeds and user is existing`() =
        runBlockingTest {
            // Arrange
            val authResult = mockk<FirebaseAuth>(relaxed = true)
            val googleSignInAccount = mockk<GoogleSignInAccount>()
            val googleSignInCredential = mockk<GoogleSignInCredential>()
            val firebaseUser = mockk<FirebaseUser>()

            coEvery { authResult.signInWithCredential(googleSignInCredential) } returns mockk {
                every { user } returns firebaseUser
                every { additionalUserInfo } returns mockk {
                    every { isNewUser } returns false
                }
            }

            // Act
            val result = firebaseAuthRepository.firebaseSignInWithGoogle(
                googleSignInAccount,
                googleSignInCredential
            )

            // Assert
            assertTrue(result is FirebaseResult.Success)
            verify { authResult.signInWithCredential(googleSignInCredential) }
        }

    @Test
    fun `firebaseSignInWithGoogle() should return failure if sign-in with credentials fails`() =
        runBlockingTest {
            // Arrange
            val authResult = mockk<FirebaseAuth>(relaxed = true)
            val googleSignInAccount = mockk<GoogleSignInAccount>()
            val googleSignInCredential = mockk<GoogleSignInCredential>()

            coEvery { authResult.signInWithCredential(googleSignInCredential) } returns mockk {
                every { exception } returns Exception()
            }

            // Act
            val result = firebaseAuthRepository.firebaseSignInWithGoogle(
                googleSignInAccount,
                googleSignInCredential
            )

            // Assert
            assertTrue(result is FirebaseResult.Failure)
            verify { authResult.signInWithCredential(googleSignInCredential) }
        }

    @Test
    fun `firebaseSignOut() should return success`() = runBlockingTest {
        // Arrange
        val authResult = mockk<FirebaseAuth>(relaxed = true)
        every { authResult.signOut() } just Runs

        // Act
        val result = firebaseAuthRepository.firebaseSignOut()

        // Assert
        assertTrue(result is FirebaseResult.Success)
        verify { authResult.signOut() }
    }
    */


