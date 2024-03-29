package com.studhub.app.domain.usecase

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.UserRepository
import com.studhub.app.domain.usecase.user.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Test
import kotlin.random.Random

class UserUseCaseTest {
    private val userDB = HashMap<String, User>()
    private val loggedInUser = User(id = "user-uid", userName = "John Doe")

    init {
        userDB[loggedInUser.id] = loggedInUser
    }

    private val authRepository: AuthRepository = object : AuthRepository {
        override val isUserAuthenticatedInFirebase: Boolean
            get() = true
        override val isEmailVerified: Boolean
            get() = true
        override val currentUserUid: String
            get() = loggedInUser.id

        override suspend fun signUpWithEmailAndPassword(
            email: String,
            password: String
        ): Flow<ApiResponse<Boolean>> = flowOf(ApiResponse.Loading)

        override suspend fun sendEmailVerification(): Flow<ApiResponse<Boolean>> =
            flowOf(ApiResponse.Loading)

        override suspend fun signInWithEmailAndPassword(
            email: String,
            password: String
        ): Flow<ApiResponse<Boolean>> = flowOf(ApiResponse.Loading)

        override suspend fun sendPasswordResetEmail(email: String): Flow<ApiResponse<Boolean>> {
            TODO("Not yet implemented")
        }

        override suspend fun signOut(): Flow<ApiResponse<Boolean>> = flowOf(ApiResponse.Loading)
        override suspend fun reloadUser(): Flow<ApiResponse<Boolean>> = flowOf(ApiResponse.Loading)

        override fun getAuthState(): Flow<Boolean> = flowOf(true)

    }

    private val listingDB = HashMap<String, Listing>()

    private val repository: UserRepository = object : UserRepository {
        override suspend fun createUser(user: User): Flow<ApiResponse<User>> {
            return flow {
                emit(ApiResponse.Loading)
                userDB[user.id] = user
                emit(ApiResponse.Success(user))
            }
        }

        override suspend fun getUser(userId: String): Flow<ApiResponse<User>> {
            return flow {
                emit(ApiResponse.Loading)
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
                emit(ApiResponse.Loading)
                if (userDB.containsKey(userId)) {
                    userDB.remove(userId)
                    emit(ApiResponse.Success(true))
                } else {
                    emit(ApiResponse.Failure("No entry for this key"))
                }
            }
        }

        override suspend fun addFavoriteListing(
            userId: String,
            favListing: Listing
        ): Flow<ApiResponse<User>> {
            return flow {
                emit(ApiResponse.Loading)
                if (userDB.containsKey(userId)) {
                    val user = userDB.getValue(userId)
                    val updatedFavoriteListings =
                        user.favoriteListings.toMutableMap().apply { put(favListing.id, true) }
                    val updatedUser = user.copy(favoriteListings = updatedFavoriteListings)
                    userDB[userId] = updatedUser
                    emit(ApiResponse.Success(updatedUser))
                } else {
                    emit(ApiResponse.Failure("No entry for this key"))
                }
            }
        }

        override suspend fun removeFavoriteListing(
            userId: String,
            favListing: Listing
        ): Flow<ApiResponse<User>> {
            return flow {
                emit(ApiResponse.Loading)
                if (userDB.containsKey(userId)) {
                    val user = userDB[userId]!!
                    val updatedFavoriteListings =
                        user.favoriteListings.toMutableMap().apply { remove(favListing.id) }
                    val updatedUser = user.copy(favoriteListings = updatedFavoriteListings)
                    userDB[userId] = updatedUser

                    emit(ApiResponse.Success(updatedUser))
                } else {
                    emit(ApiResponse.Failure("No entry for this key"))
                }
            }
        }

        override suspend fun getFavoriteListings(userId: String): Flow<ApiResponse<List<Listing>>> {
            return flow {
                emit(ApiResponse.Loading)
                if (userDB.containsKey(userId)) {
                    val user = userDB[userId]!!
                    val favoriteListings = mutableListOf<Listing>()
                    user.favoriteListings.forEach {
                        favoriteListings.add(listingDB[it.key]!!)
                    }
                    emit(ApiResponse.Success(favoriteListings))
                } else {
                    emit(ApiResponse.Failure("No entry for this key"))
                }
            }
        }

        override suspend fun blockUser(
            userId: String,
            blockedUserId: String
        ): Flow<ApiResponse<User>> {
            return flow {
                emit(ApiResponse.Loading)
                if (userDB.containsKey(userId)) {
                    val user = userDB.getValue(userId)
                    val updatedBlockedUsers =
                        user.blockedUsers.toMutableMap().apply { put(blockedUserId, true) }
                    val updatedUser = user.copy(blockedUsers = updatedBlockedUsers)
                    userDB[userId] = updatedUser
                    emit(ApiResponse.Success(updatedUser))
                } else {
                    emit(ApiResponse.Failure("No entry for this key"))
                }
            }
        }

        override suspend fun unblockUser(
            userId: String,
            blockedUserId: String
        ): Flow<ApiResponse<User>> {
            return flow {
                emit(ApiResponse.Loading)
                if (userDB.containsKey(userId)) {
                    val user = userDB[userId]!!
                    val updatedBlockedUsers =
                        user.blockedUsers.toMutableMap().apply { remove(blockedUserId) }
                    val updatedUser = user.copy(blockedUsers = updatedBlockedUsers)
                    userDB[userId] = updatedUser

                    emit(ApiResponse.Success(updatedUser))
                } else {
                    emit(ApiResponse.Failure("No entry for this key"))
                }
            }
        }

        override suspend fun addRating(userId: String, rating: Rating): Flow<ApiResponse<Rating>> {
            TODO("Not yet implemented")
        }

        override suspend fun updateRating(
            userId: String,
            ratingId: String,
            rating: Rating
        ): Flow<ApiResponse<Rating>> {
            TODO("Not yet implemented")
        }

        override suspend fun deleteRating(
            userId: String,
            ratingId: String
        ): Flow<ApiResponse<Boolean>> {
            TODO("Not yet implemented")
        }

        override suspend fun getUserRatings(userId: String): Flow<ApiResponse<List<Rating>>> {
            TODO("Not yet implemented")
        }

        override suspend fun getBlockedUsers(userId: String): Flow<ApiResponse<List<User>>> {
            return flow {
                emit(ApiResponse.Loading)
                if (userDB.containsKey(userId)) {
                    val user = userDB[userId]!!
                    val blockedUsers = mutableListOf<User>()
                    user.blockedUsers.forEach {
                        blockedUsers.add(userDB[it.key]!!)
                    }
                    emit(ApiResponse.Success(blockedUsers))
                } else {
                    emit(ApiResponse.Failure("No entry for this key"))
                }
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
        val updateCurrentUserInfo = UpdateCurrentUserInfo(repository, authRepository)

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

    @Test
    fun addFavoriteListingAddsCorrectListingToUserFavorites() = runBlocking {
        val addFavoriteListing = AddFavoriteListing(repository, authRepository)
        val userId = authRepository.currentUserUid
        val listingId = Random.nextLong().toString()
        userDB[userId] = User(id = userId, userName = "Test User", favoriteListings = emptyMap())

        addFavoriteListing(Listing(id = listingId)).collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val user = response.data
                    Assert.assertNotNull(user)
                    Assert.assertEquals(mapOf(listingId to true), user.favoriteListings)
                }
                is ApiResponse.Failure -> Assert.fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun addBlockedUserAddsCorrectUserToBlockedUserList() = runBlocking {
        val addBlockedUser = AddBlockedUser(repository, authRepository)
        val userId = authRepository.currentUserUid
        val blockedUserId = Random.nextLong().toString()
        userDB[userId] = User(id = userId, userName = "Test User", blockedUsers = emptyMap())

        addBlockedUser(blockedUserId).collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val user = response.data
                    Assert.assertNotNull(user)
                    Assert.assertEquals(mapOf(blockedUserId to true), user.blockedUsers)
                }
                is ApiResponse.Failure -> Assert.fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun removeFavoriteListingRemovesCorrectListingFromUserFavorites() = runBlocking {
        val removeFavoriteListing = RemoveFavoriteListing(repository, authRepository)

        val userId = authRepository.currentUserUid
        val listingId1 = Random.nextLong().toString()
        val listingId2 = Random.nextLong().toString()
        userDB[userId] = User(
            id = userId,
            userName = "Test User",
            favoriteListings = mapOf(listingId1 to true, listingId2 to true)
        )

        removeFavoriteListing(Listing(id = listingId1)).collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val result = response.data
                    Assert.assertNotNull(result)
                    val user = userDB.getValue(userId)
                    Assert.assertEquals(mapOf(listingId2 to true), user.favoriteListings)
                }
                is ApiResponse.Failure -> Assert.fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun unblockUserRemovesCorrectUserFromBlockedUsersList() = runBlocking {
        val unblockUser = UnblockUser(repository, authRepository)
        val userId = authRepository.currentUserUid
        val blockedUserId1 = Random.nextLong().toString()
        val blockedUserId2 = Random.nextLong().toString()
        userDB[userId] = User(
            id = userId,
            userName = "Test User",
            blockedUsers = mapOf(blockedUserId1 to true, blockedUserId2 to true)
        )

        unblockUser(blockedUserId1).collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val result = response.data
                    Assert.assertNotNull(result)
                    val user = userDB.getValue(userId)
                    Assert.assertEquals(mapOf(blockedUserId2 to true), user.blockedUsers)
                }
                is ApiResponse.Failure -> Assert.fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun getFavoriteListingsReturnsCorrectListings() = runBlocking {
        val getFavoriteListings = GetFavoriteListings(repository, authRepository)

        val userId = authRepository.currentUserUid
        val listing1 = Listing(id = Random.nextLong().toString(), name = "Test Listing 1")
        val listing2 = Listing(id = Random.nextLong().toString(), name = "Test Listing 2")
        listingDB[listing1.id] = listing1
        listingDB[listing2.id] = listing2
        userDB[userId] = User(
            id = userId,
            userName = "Test User",
            favoriteListings = mapOf(listing1.id to true, listing2.id to true)
        )

        getFavoriteListings().collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val result = response.data
                    Assert.assertNotNull(result)
                    Assert.assertEquals(2, result.size)
                    Assert.assertTrue(result.contains(listing1))
                    Assert.assertTrue(result.contains(listing2))
                }
                is ApiResponse.Failure -> Assert.fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun getUserUseCaseReturnsFailureForInvalidUserId() = runBlocking {
        val getUser = GetUser(repository)

        // Call the use case with an invalid user ID
        val userId = "invalid-id"
        getUser(userId).collect { response ->
            when (response) {
                is ApiResponse.Success -> Assert.fail("Should not succeed with invalid user ID")
                is ApiResponse.Failure -> {
                    // Make sure the error message contains the expected text
                    val expectedErrorMessage = "No entry for this key"
                    Assert.assertTrue(response.message.contains(expectedErrorMessage))
                }
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun getBlockedUsersReturnsCorrectUsers() = runBlocking {
        val getBlockedUsers = GetBlockedUsers(repository, authRepository)

        val userId = authRepository.currentUserUid
        val user1 = User(id = Random.nextLong().toString(), userName = "Test User 1")
        val user2 = User(id = Random.nextLong().toString(), userName = "Test User 2")
        userDB[user1.id] = user1
        userDB[user2.id] = user2
        userDB[userId] = User(
            id = userId,
            userName = "Test User",
            blockedUsers = mapOf(user1.id to true, user2.id to true)
        )

        getBlockedUsers().collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val result = response.data
                    Assert.assertNotNull(result)
                    Assert.assertEquals(2, result.size)
                    Assert.assertTrue(result.contains(user1))
                    Assert.assertTrue(result.contains(user2))
                }
                is ApiResponse.Failure -> Assert.fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }
}
