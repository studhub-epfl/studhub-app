package com.studhub.app.domain.usecase

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
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
        override val currentUserUid: String
            get() = loggedInUser.id

        override suspend fun oneTapSignInWithGoogle(): Flow<ApiResponse<BeginSignInResult>> =
            flowOf(ApiResponse.Loading)

        override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Flow<ApiResponse<Boolean>> =
            flowOf(ApiResponse.Loading)

        override suspend fun signOut(): Flow<ApiResponse<Boolean>> = flowOf(ApiResponse.Loading)

    }

    private val listingDB = HashMap<String, Listing>()

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
                emit(ApiResponse.Loading)
                delay(1000)
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
            favListingId: String
        ): Flow<ApiResponse<User>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                if (userDB.containsKey(userId)) {
                    val user = userDB.getValue(userId)
                    if (user.favoriteListings.contains(favListingId)) {
                        emit(ApiResponse.Failure("Listing is already a favorite"))
                    } else {
                        val updatedFavoriteListings =
                            user.favoriteListings.toMutableList().apply { add(favListingId) }
                        val updatedUser = user.copy(favoriteListings = updatedFavoriteListings)
                        userDB[userId] = updatedUser
                        emit(ApiResponse.Success(updatedUser))
                    }
                } else {
                    emit(ApiResponse.Failure("No entry for this key"))
                }
            }
        }

        override suspend fun removeFavoriteListing(
            userId: String,
            favListingId: String
        ): Flow<ApiResponse<Boolean>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                if (userDB.containsKey(userId)) {
                    val user = userDB[userId]!!
                    if (!user.favoriteListings.contains(favListingId)) {
                        emit(ApiResponse.Failure("Listing is not a favorite"))
                        return@flow
                    }

                    val updatedFavoriteListings =
                        user.favoriteListings.toMutableList().apply { remove(favListingId) }
                    val updatedUser = user.copy(favoriteListings = updatedFavoriteListings)
                    userDB[userId] = updatedUser

                    emit(ApiResponse.Success(true))
                } else {
                    emit(ApiResponse.Failure("No entry for this key"))
                }
            }
        }

        override suspend fun getFavoriteListings(userId: String): Flow<ApiResponse<List<Listing>>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                if (userDB.containsKey(userId)) {
                    val user = userDB[userId]!!
                    val favoriteListings = user.favoriteListings.mapNotNull { listingId ->
                        listingDB[listingId]
                    }
                    emit(ApiResponse.Success(favoriteListings))
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
        userDB[userId] = User(id = userId, userName = "Test User", favoriteListings = emptyList())

        addFavoriteListing(listingId).collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val user = response.data
                    Assert.assertNotNull(user)
                    Assert.assertEquals(listOf(listingId), user.favoriteListings)
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
            favoriteListings = listOf(listingId1, listingId2)
        )

        removeFavoriteListing(listingId1).collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val result = response.data
                    Assert.assertNotNull(result)
                    Assert.assertTrue(result)
                    val user = userDB.getValue(userId)
                    Assert.assertEquals(listOf(listingId2), user.favoriteListings)
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
            favoriteListings = listOf(listing1.id, listing2.id)
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
}
