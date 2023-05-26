package com.studhub.app.data.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.network.NetworkStatus
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {

    @Mock
    private lateinit var auth: FirebaseAuth

    @Mock
    private lateinit var firebaseUser: FirebaseUser

    @Mock
    private lateinit var db: FirebaseDatabase

    @Mock
    private lateinit var networkStatus: NetworkStatus

    @Mock
    private lateinit var authResult: AuthResult

    @Inject
    lateinit var localDb: LocalDataSource

    private lateinit var repository: AuthRepositoryImpl

    @Mock
    private lateinit var dbRef: DatabaseReference

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun setUp() {
        hiltRule.inject()
        MockitoAnnotations.openMocks(this)
        `when`(db.getReference("users")).thenReturn(dbRef)
        `when`(db.getReference(anyString())).thenReturn(dbRef)
        `when`(dbRef.child(anyString())).thenReturn(dbRef)
        `when`(auth.currentUser).thenReturn(firebaseUser)
        repository = AuthRepositoryImpl(auth, db, localDb, networkStatus)
    }

    @Test
    fun signUpWithEmailAndPasswordWithNoInternetConnection() = runBlocking {
        `when`(networkStatus.isConnected).thenReturn(false)

        repository.signUpWithEmailAndPassword("test@epfl.ch", "password").collect {
            when (it) {
                is ApiResponse.Success -> fail("No internet connection so no success")
                is ApiResponse.Failure -> assertTrue(true)
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun signUpWithEmailAndPasswordWithIncorrectEmail() = runBlocking {
        `when`(networkStatus.isConnected).thenReturn(true)

        repository.signUpWithEmailAndPassword("test@gmail.com", "password").collect {
            when (it) {
                is ApiResponse.Success -> fail("Email should not be authorized")
                is ApiResponse.Failure -> assertTrue(true)
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun signInWithEmailAndPasswordWithNoInternetConnection() = runBlocking {
        `when`(networkStatus.isConnected).thenReturn(false)

        repository.signInWithEmailAndPassword("test@epfl.ch", "password").collect {
            when (it) {
                is ApiResponse.Success -> fail("No internet connection so no success")
                is ApiResponse.Failure -> assertTrue(true)
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun signInWithEmailAndPassword() = runBlocking {
        `when`(networkStatus.isConnected).thenReturn(true)
        `when`(authResult.user).thenReturn(firebaseUser)
        `when`(
            auth.signInWithEmailAndPassword(
                anyString(),
                anyString()
            )
        ).thenReturn(Tasks.forResult(authResult))

        repository.signInWithEmailAndPassword("test@epfl.com", "password").collect {
            when (it) {
                is ApiResponse.Success -> assertTrue(true)
                is ApiResponse.Failure -> fail("Email should not be authorized")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Mock
    private lateinit var authCredential: AuthCredential

    @Test
    fun signUpWithEmailAndPasswordWithCorrectEmail() = runBlocking {
        `when`(networkStatus.isConnected).thenReturn(true)
        `when`(firebaseUser.uid).thenReturn("random")
        `when`(authResult.user).thenReturn(firebaseUser)
        `when`(authResult.credential).thenReturn(authCredential)
        `when`(
            auth.createUserWithEmailAndPassword(
                anyString(),
                anyString()
            )
        ).thenReturn(Tasks.forResult(null))


        repository.signUpWithEmailAndPassword("test@epfl.ch", "password").collect {
            when (it) {
                is ApiResponse.Success -> assertTrue(true)
                is ApiResponse.Failure -> fail("Email should be authorized")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun signOut() = runBlocking {
        `when`(auth.signOut()).then { }

        repository.signOut().collect {
            when (it) {
                is ApiResponse.Success -> assertTrue(true)
                is ApiResponse.Failure -> fail("Should not fail")
                is ApiResponse.Loading -> {}
            }
        }
    }


    @Test
    fun sendPasswordResetEmailWithNoInternetConnection() = runBlocking {
        `when`(networkStatus.isConnected).thenReturn(false)

        repository.sendPasswordResetEmail("test@epfl.ch").collect {
            when (it) {
                is ApiResponse.Success -> fail("No internet connection so no success")
                is ApiResponse.Failure -> assertTrue(true)
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun sendPasswordResetEmail() = runBlocking {
        `when`(networkStatus.isConnected).thenReturn(true)
        `when`(auth.sendPasswordResetEmail(anyString())).thenReturn(Tasks.forResult(null))

        repository.sendPasswordResetEmail("test@epfl.ch").collect {
            when (it) {
                is ApiResponse.Success -> assertTrue(true)
                is ApiResponse.Failure -> fail("Should not fail")
                is ApiResponse.Loading -> {}
            }
        }
    }


    @Test
    fun sendEmailVerificationWithNoInternetConnection() = runBlocking {
        `when`(networkStatus.isConnected).thenReturn(false)

        repository.sendEmailVerification().collect {
            when (it) {
                is ApiResponse.Success -> fail("No internet connection so no success")
                is ApiResponse.Failure -> assertTrue(true)
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun sendEmailVerification() = runBlocking {
        `when`(networkStatus.isConnected).thenReturn(true)
        `when`(firebaseUser.sendEmailVerification()).thenReturn(Tasks.forResult(null))

        repository.sendEmailVerification().collect {
            when (it) {
                is ApiResponse.Success -> assertTrue(true)
                is ApiResponse.Failure -> fail("Should not fail")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun reloadUserWithNoInternetConnection() = runBlocking {
        `when`(networkStatus.isConnected).thenReturn(false)

        repository.reloadUser().collect {
            when (it) {
                is ApiResponse.Success -> fail("No internet connection so no success")
                is ApiResponse.Failure -> assertTrue(true)
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun reloadUser() = runBlocking {
        `when`(networkStatus.isConnected).thenReturn(true)
        `when`(auth.currentUser).thenReturn(firebaseUser)
        `when`(firebaseUser.reload()).thenReturn(Tasks.forResult(null))
        `when`(
            auth.signInWithEmailAndPassword(
                anyString(),
                anyString()
            )
        ).thenReturn(Tasks.forResult(authResult))

        repository.reloadUser().collect {
            when (it) {
                is ApiResponse.Success -> assertTrue(true)
                is ApiResponse.Failure -> fail("Should not be fail")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun firebaseUserToUser() {
        val id = "user-id"
        `when`(firebaseUser.uid).thenReturn(id)
        val user = firebaseUser.toUser()
        assertEquals("User Id is correctly passed", id, user.id)
    }
}
