package com.studhub.app.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.database.FirebaseDatabase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.network.NetworkStatus
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import com.studhub.app.domain.usecase.user.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UserRepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepo: UserRepository

    @Inject
    lateinit var getUser: GetUser

    @Inject
    lateinit var remoteDb: FirebaseDatabase

    @Inject
    lateinit var localDb: LocalDataSource

    @Inject
    lateinit var networkStatus: NetworkStatus


    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun setAndGetSameUser() {
        val userRepo = UserRepositoryImpl(remoteDb, localDb, networkStatus) // real repo
        val getUser = GetUser(userRepo)
        lateinit var user: User

        runBlocking {

            val edouard = User(
                userName = "edouardmichelin",
                firstName = "Edouard",
                lastName = "Michelin",
                email = "edouard.michelin@epfl.ch",
                phoneNumber = "0799531499",
                profilePicture = "https://studhub.com/profile-pictures/${Random.nextLong()}.jpg"
            )

            userRepo.createUser(edouard).collect {
                when (it) {
                    is ApiResponse.Success -> user = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }

        runBlocking {
            getUser(user.id).collect {
                when (it) {
                    is ApiResponse.Success -> assert(it.data == user)
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }

        }
    }

    @Test
    fun addAndGetFavoriteListing() {
        val userRepo = UserRepositoryImpl(remoteDb, localDb, networkStatus) // real repo
        val authRepo = MockAuthRepositoryImpl() // fake repo
        val addFavoriteListing = AddFavoriteListing(userRepo, authRepo)
        val getFavoriteListings = GetFavoriteListings(userRepo, authRepo)
        val product = Listing(
            id = Random.nextLong().toString(),
            name = "Testing Product ${Random.nextLong()}",
        )

        runBlocking {
            addFavoriteListing(product).collect() {
                when (it) {
                    is ApiResponse.Failure -> fail(it.message)
                    ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {}
                }
            }
        }

        runBlocking {
            getFavoriteListings().collect {
                when (it) {
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Success -> listOf(product) == it.data
                }
            }
        }
    }

    @Test
    fun addAndRemoveFavoriteListing() {
        val userRepo = UserRepositoryImpl(remoteDb, localDb, networkStatus) // real repo
        val authRepo = MockAuthRepositoryImpl() // fake repo
        val addFavoriteListing = AddFavoriteListing(userRepo, authRepo)
        val removeFavoriteListing = RemoveFavoriteListing(userRepo, authRepo)
        val product = Listing(
            id = Random.nextLong().toString(),
            name = "Testing Product ${Random.nextLong()}",
        )

        runBlocking {
            addFavoriteListing(product).collect {
                when (it) {
                    is ApiResponse.Failure -> fail(it.message)
                    ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {}
                }
            }
        }

        runBlocking {
            removeFavoriteListing(product).collect {
                when (it) {
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {}
                }
            }
        }
    }

    @Test
    fun addAndRemoveBlockedUser() {
        val userRepo = UserRepositoryImpl(remoteDb, localDb, networkStatus) // real repo
        val authRepo = MockAuthRepositoryImpl() // fake repo
        val addBlockedUser = AddBlockedUser(userRepo, authRepo)
        val removeBlockedUser = UnblockUser(userRepo, authRepo)
        val user = User(
            id = Random.nextLong().toString(),
            userName = "Testing User ${Random.nextLong()}",
        )

        runBlocking {
            addBlockedUser(user.id).collect() {
                when (it) {
                    is ApiResponse.Failure -> fail(it.message)
                    ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {}
                }
            }
        }
        runBlocking {
            removeBlockedUser(user.id).collect {
                when (it) {
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {}
                }
            }
        }
    }

    @Test
    fun updateRating() = runBlocking {
        val userRepo = UserRepositoryImpl(remoteDb, localDb, networkStatus)
        val user = User(
            userName = "testUser",
            firstName = "Test",
            lastName = "User",
            email = "test.user@epfl.ch",
            phoneNumber = "0799531499",
            profilePicture = "https://studhub.com/profile-pictures/${Random.nextLong()}.jpg"
        )

        val rating = Rating(
            userId = user.id,
            id = Random.nextInt().toString()
        )

        userRepo.addRating(user.id, rating).collect {
            when (it) {
                is ApiResponse.Success -> {}
                is ApiResponse.Failure -> fail(it.message)
                is ApiResponse.Loading -> {}
            }
        }

        val updatedRating = Rating(
            id = rating.id,
            userId = user.id,
        )

        userRepo.updateRating(user.id, rating.id, updatedRating).collect {
            when (it) {
                is ApiResponse.Success -> assert(it.data == updatedRating)
                is ApiResponse.Failure -> fail(it.message)
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun deleteRating() = runBlocking {
        val userRepo = UserRepositoryImpl(remoteDb, localDb, networkStatus)
        val user = User(
            userName = "testUser",
            firstName = "Test",
            lastName = "User",
            email = "test.user@epfl.ch",
            phoneNumber = "0799531499",
            profilePicture = "https://studhub.com/profile-pictures/${Random.nextLong()}.jpg"
        )

        val rating = Rating(
            userId = user.id,
            id = Random.nextInt().toString()
        )

        userRepo.addRating(user.id, rating).collect {
            when (it) {
                is ApiResponse.Success -> {
                }

                is ApiResponse.Failure -> fail(it.message)
                is ApiResponse.Loading -> {}
            }
        }

        userRepo.deleteRating(user.id, rating.id).collect {
            when (it) {
                is ApiResponse.Success -> assert(it.data == true)
                is ApiResponse.Failure -> fail(it.message)
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun getUserRatings() = runBlocking {
        val userRepo = UserRepositoryImpl(remoteDb, localDb, networkStatus)
        val userId = UUID.randomUUID().toString()
        val user = User(
            id = userId,
            userName = "testUser",
            firstName = "Test",
            lastName = "User",
            email = "test.user@epfl.ch",
            phoneNumber = "0799531499",
            profilePicture = "https://studhub.com/profile-pictures/${Random.nextLong()}.jpg"
        )

        val ratingId = UUID.randomUUID().toString()
        val timestampd= Random.nextLong()
        val rating = Rating(
            userId = userId,
            id = ratingId,
            timestamp = timestampd

        )

        userRepo.addRating(user.id, rating).collect {
            when (it) {
                is ApiResponse.Success -> {}
                is ApiResponse.Failure -> fail(it.message)
                is ApiResponse.Loading -> {}
            }
        }

        delay(1000)

        userRepo.getUserRatings(user.id).collectLatest  {
            when (it) {
                is ApiResponse.Success -> {
                    val ratingExists = it.data.any { it.userId == rating.userId && it.timestamp == timestampd }
                    assert(ratingExists)
                }
                is ApiResponse.Failure -> fail(it.message)
                is ApiResponse.Loading -> {}
            }
        }

    }

    @Test
    fun blockUser() = runBlocking {
        val userRepo = UserRepositoryImpl(remoteDb, localDb, networkStatus)
        val user1 = User(
            id = UUID.randomUUID().toString(),
            userName = "testUser1",
            firstName = "Test1",
            lastName = "User1",
            email = "test.user1@epfl.ch",
            phoneNumber = "0799531499",
            profilePicture = "https://studhub.com/profile-pictures/${Random.nextLong()}.jpg"
        )

        val user2 = User(
            id = UUID.randomUUID().toString(),
            userName = "testUser2",
            firstName = "Test2",
            lastName = "User2",
            email = "test.user2@epfl.ch",
            phoneNumber = "0799531499",
            profilePicture = "https://studhub.com/profile-pictures/${Random.nextLong()}.jpg"
        )

        userRepo.blockUser(user1.id, user2.id).collect {
            when (it) {
                is ApiResponse.Success -> assert(it.data.blockedUsers.contains(user2.id))
                is ApiResponse.Failure -> fail(it.message)
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun unblockUser() = runBlocking {
        val userRepo = UserRepositoryImpl(remoteDb, localDb, networkStatus)
        val user1 = User(
            id = UUID.randomUUID().toString(),
            userName = "testUser1",
            firstName = "Test1",
            lastName = "User1",
            email = "test.user1@epfl.ch",
            phoneNumber = "0799531499",
            profilePicture = "https://studhub.com/profile-pictures/${Random.nextLong()}.jpg"
        )

        val user2 = User(
            id = UUID.randomUUID().toString(),
            userName = "testUser2",
            firstName = "Test2",
            lastName = "User2",
            email = "test.user2@epfl.ch",
            phoneNumber = "0799531499",
            profilePicture = "https://studhub.com/profile-pictures/${Random.nextLong()}.jpg"
        )

        userRepo.blockUser(user1.id, user2.id).collect {
            when (it) {
                is ApiResponse.Success -> {}
                is ApiResponse.Failure -> fail(it.message)
                is ApiResponse.Loading -> {}
            }
        }

        userRepo.unblockUser(user1.id, user2.id).collect {
            when (it) {
                is ApiResponse.Success -> assert(!it.data.blockedUsers.contains(user2.id))
                is ApiResponse.Failure -> fail(it.message)
                is ApiResponse.Loading -> {}
            }
        }
    }



}
