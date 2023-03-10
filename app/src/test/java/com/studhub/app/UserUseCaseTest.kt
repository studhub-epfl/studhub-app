package com.studhub.app

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import com.studhub.app.domain.usecase.listing.CreateListing
import com.studhub.app.domain.usecase.user.CreateUser
import com.studhub.app.domain.usecase.user.GetUser
import com.studhub.app.domain.usecase.user.UpdateUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Test
import kotlin.random.Random

class UserUseCaseTest {
    private val userDB = HashMap<Long, User>()

    private val repository: UserRepository = object : UserRepository {
        override suspend fun createUser(user: User): Flow<ApiResponse<Boolean>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                userDB[user.id] = user
                emit(ApiResponse.Success(true))
            }
        }

        override suspend fun getUser(userId: Long): Flow<ApiResponse<User>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                if (userDB.containsKey(userId))
                    emit(ApiResponse.Success(userDB.getValue(userId)))
                else
                    emit(ApiResponse.Failure("No entry for this key"))
            }
        }

        override suspend fun updateUser(userId: Long, updatedUser: User): Flow<ApiResponse<User>> {
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

        override suspend fun removeUser(userId: Long): Flow<ApiResponse<Boolean>> {
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
    fun `Create User Use Case creates the correct entry in the given repository`() = runBlocking {
        val createUser = CreateUser(repository)

        val userId = Random.nextLong()
        val userName = Random.nextLong().toString()
        val listing = User(id = userId, userName = userName)

        createUser(listing).collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val result = response.data
                    Assert.assertTrue(result)
                    Assert.assertEquals(userName, userDB.getValue(userId).userName)
                }
                is ApiResponse.Failure -> Assert.fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun `Get User Use Case retrieves the correct entry of the given repository`() = runBlocking {
        val getUser = GetUser(repository)

        val userId = Random.nextLong()
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
    fun `Update User Use Case retrieves the correct entry of the given repository`() = runBlocking {
        val updateUser = UpdateUser(repository)

        val userId1 = Random.nextLong()
        val userName1 = Random.nextLong().toString()
        val userId2 = Random.nextLong()
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
}
