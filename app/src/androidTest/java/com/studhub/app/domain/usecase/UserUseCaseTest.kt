package com.studhub.app.domain.usecase

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.UserRepository
import com.studhub.app.domain.usecase.user.GetUser
import com.studhub.app.domain.usecase.user.UpdateCurrentUserInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Test
import kotlin.random.Random
import kotlin.random.nextLong

class UserUseCaseTest {
    private val userDB = HashMap<String, User>()
    private val loggedInUser = User(id = "user-uid", userName = "John Doe" )

    init {
        userDB[loggedInUser.id] = loggedInUser
    }

    private val authRepo: AuthRepository = object : AuthRepository {
        override val isUserAuthenticatedInFirebase: Boolean
            get() = true
        override val currentUserUid: String
            get() = loggedInUser.id

        override suspend fun oneTapSignInWithGoogle(): Flow<ApiResponse<BeginSignInResult>> =
            flowOf(ApiResponse.Loading)

        override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Flow<ApiResponse<Boolean>> =
            flowOf(ApiResponse.Loading)

        override suspend fun signOut(): Flow<ApiResponse<Boolean>> = flowOf(ApiResponse.Loading)

    }

    private val repository: UserRepository = object : UserRepository {
        override suspend fun createUser(user: User): Flow<ApiResponse<User>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                userDB[user.id] = user
                emit(ApiResponse.Success(user))
            }
        }

        override suspend fun getUser(userId: String): Flow<ApiResponse<User>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                if (userDB.containsKey(userId))
                    emit(ApiResponse.Success(userDB.getValue(userId)))
                else
                    emit(ApiResponse.Failure("No entry for this key"))
            }
        }

        override suspend fun updateUserInfo(
            userId: String,
            updatedUser: User
        ): Flow<ApiResponse<User>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                if (userDB.containsKey(userId)) {
                    val newListing = updatedUser.copy(id = userId)
                    userDB[userId] = newListing
                    emit(ApiResponse.Success(userDB.getValue(userId)))
                } else
                    emit(ApiResponse.Failure("No entry for this key"))
            }
        }

        override suspend fun removeUser(userId: String): Flow<ApiResponse<Boolean>> {
            return flow {
                emit(ApiResponse.Success(true))
            }
        }

    }

    @After
    fun clearDB() {
        userDB.clear()
    }

    @Test
    fun getUserUseCaseRetrievesCorrectEntryFromGivenRepository() = runBlocking {
        val getUser = GetUser(repository)

        val userId = Random.nextLong().toString()
        val userName = Random.nextLong().toString()
        userDB[userId] = User(id = userId, userName = userName)

        getUser(userId).collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val user = response.data
                    Assert.assertNotNull(user)
                    Assert.assertEquals(userName, user.userName)
                }
                is ApiResponse.Failure -> Assert.fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun updateUserUseCaseUpdatesCorrectEntryInGivenRepository() = runBlocking {
        val updateCurrentUserInfo = UpdateCurrentUserInfo(repository, authRepo)

        val userId = Random.nextLong().toString()
        val userName = Random.nextLong().toString()
        val email = "${Random.nextLong()}@stud-hub.com"

        val updatedUser = User(id = userId, userName = userName, email = email)

        updateCurrentUserInfo(updatedUser).collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val result: User = response.data

                    Assert.assertEquals(
                        "user id should not be updated",
                        result.id,
                        loggedInUser.id
                    )

                    Assert.assertEquals(
                        "email should be updated",
                        result.email,
                        email
                    )

                    Assert.assertEquals(
                        "username should be updated",
                        result.userName,
                        userName
                    )
                }
                is ApiResponse.Failure -> Assert.fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }
}
