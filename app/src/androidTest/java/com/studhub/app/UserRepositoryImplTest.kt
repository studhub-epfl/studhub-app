package com.studhub.app

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.repository.UserRepositoryImpl
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.user.CreateUser
import com.studhub.app.domain.usecase.user.GetUser
import kotlinx.coroutines.runBlocking
import org.junit.Assert.fail
import org.junit.Test
import kotlin.random.Random

class UserRepositoryImplTest {

    @Test
    fun setAndGetSameUser() {
        val repository = UserRepositoryImpl()
        val createUser = CreateUser(repository)
        val getUser = GetUser(repository)

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
