package com.studhub.app.data.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ListingRepository
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@Singleton
class MockUserRepositoryImpl: UserRepository {
    private val userDB = HashMap<String, User>()

    init {
        userDB[MockAuthRepositoryImpl.loggedInUser.id] = MockAuthRepositoryImpl.loggedInUser
    }

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

    override suspend fun updateUser(userId: String, updatedUser: User): Flow<ApiResponse<User>> {
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
            emit(ApiResponse.Success(true))
        }
    }
}
