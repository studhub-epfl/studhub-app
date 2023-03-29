package com.studhub.app.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import com.studhub.app.domain.usecase.user.AddFavoriteListing
import com.studhub.app.domain.usecase.user.GetFavoriteListings
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.random.Random

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UserRepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepo: UserRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun setAndGetSameUser() {

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
            userRepo.getUser(user.id).collect {
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
        val userRepo = UserRepositoryImpl() // real repo
        val authRepo = MockAuthRepositoryImpl() // fake repo
        val addFavoriteListing = AddFavoriteListing(userRepo, authRepo)
        val getFavoriteListings = GetFavoriteListings(userRepo, authRepo)
        val product = Listing(
            id = Random.nextLong().toString(),
            name = "Testing Product ${Random.nextLong()}",
        )

        runBlocking {
            addFavoriteListing(product.id).collect() {
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
}
