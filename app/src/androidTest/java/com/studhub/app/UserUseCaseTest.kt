package com.studhub.app

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import com.studhub.app.domain.usecase.user.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Test
import kotlin.random.Random

class UserUseCaseTest {
    private val userDB = HashMap<String, User>()

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

        override suspend fun updateUser(
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

        override suspend fun updateFavoriteStatus(
            userId: String,
            isFavorite: Boolean
        ): Flow<ApiResponse<User>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                if (userDB.containsKey(userId)) {
                    val updatedUser = userDB.getValue(userId).copy(isFavorite = isFavorite)
                    userDB[userId] = updatedUser
                    emit(ApiResponse.Success(updatedUser))
                } else {
                    emit(ApiResponse.Failure("No entry for this key"))
                }
            }
        }

        override suspend fun getFavoriteUsers(): Flow<ApiResponse<List<User>>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                val favoriteUsers = userDB.values.filter { it.isFavorite }
                emit(ApiResponse.Success(favoriteUsers))
            }
        }

    }

    @After
    fun clearDB() {
        userDB.clear()
    }

    @Test
    fun createUserUseCaseCorrectEntryInGivenRepository() = runBlocking {
        val createUser = CreateUser(repository)

        val userId = Random.nextLong().toString()
        val userName = Random.nextLong().toString()
        val listing = User(id = userId, userName = userName)

        createUser(listing).collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val result = response.data
                    Assert.assertNotNull(result)
                    Assert.assertEquals(userName, userDB.getValue(userId).userName)
                }
                is ApiResponse.Failure -> Assert.fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
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
        val updateUser = UpdateUser(repository)

        val userId1 = Random.nextLong().toString()
        val userName1 = Random.nextLong().toString()
        val userId2 = Random.nextLong().toString()
        val userName2 = Random.nextLong().toString()
        userDB[userId1] = User(id = userId1, userName = userName1)

        val updatedUser = User(id = userId2, userName = userName2)

        updateUser(userId1, updatedUser).collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val result: User = response.data
                    val expectedResult: User = updatedUser.copy(id = userId1)
                    val user1FromDB: User = userDB.getValue(userId1)

                    Assert.assertEquals(
                        "returned user should match the updated one",
                        result,
                        expectedResult
                    )

                    Assert.assertEquals(
                        "username should be updated",
                        userName2,
                        user1FromDB.userName
                    )

                    Assert.assertEquals("id should not be updated", userId1, user1FromDB.id)
                }
                is ApiResponse.Failure -> Assert.fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun updateFavoriteStatusReturnsSuccessWhenEntryExists() = runBlocking {
        val updateUser = UpdateFavoriteStatus(repository)

        val userId = Random.nextLong().toString()
        val userName = Random.nextLong().toString()
        userDB[userId] = User(id = userId, userName = userName, isFavorite = false)

        val updatedUser = userDB.getValue(userId).copy(isFavorite = true)

        updateUser(userId, true).collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val result: User = response.data
                    val userFromDB: User = userDB.getValue(userId)

                    Assert.assertEquals(
                        "returned user should match the updated one",
                        result,
                        updatedUser
                    )

                    Assert.assertEquals(
                        "isFavorite should be updated",
                        true,
                        userFromDB.isFavorite
                    )

                    Assert.assertEquals(
                        "username should not be updated",
                        userName,
                        userFromDB.userName
                    )
                }
                is ApiResponse.Failure -> Assert.fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun updateFavoriteStatusReturnsFailureWhenEntryDoesNotExist() = runBlocking {
        val updateUser = UpdateFavoriteStatus(repository)

        val userId = Random.nextLong().toString()

        updateUser(userId, true).collect { response ->
            when (response) {
                is ApiResponse.Success -> Assert.fail("Request success")
                is ApiResponse.Failure -> Assert.assertEquals(
                    "Error message should match",
                    "No entry for this key",
                    response.message
                )
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun getFavoriteUsersReturnsSuccessWithCorrectEntries() = runBlocking {
        val getFavoriteUsers = GetFavoriteUsers(repository)

        val userId1 = Random.nextLong().toString()
        val userName1 = Random.nextLong().toString()
        val userId2 = Random.nextLong().toString()
        val userName2 = Random.nextLong().toString()
        userDB[userId1] = User(id = userId1, userName = userName1, isFavorite = false)
        userDB[userId2] = User(id = userId2, userName = userName2, isFavorite = true)

        getFavoriteUsers().collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val favoriteUsers: List<User> = response.data

                    Assert.assertEquals(
                        "There should be only one favorite user",
                        1,
                        favoriteUsers.size
                    )

                    Assert.assertEquals(
                        "The favorite user should match",
                        userId2,
                        favoriteUsers[0].id
                    )
                }
                is ApiResponse.Failure -> Assert.fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }
}
