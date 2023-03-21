package com.studhub.app.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.repository.UserRepositoryImpl
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ListingRepository
import com.studhub.app.domain.repository.UserRepository
import com.studhub.app.domain.usecase.user.CreateUser
import com.studhub.app.domain.usecase.user.GetUser
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
    lateinit var createUser: CreateUser

    @Inject
    lateinit var getUser: GetUser

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

            createUser(edouard).collect {
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
}
