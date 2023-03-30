package com.studhub.app.data.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@Singleton
class MockUserRepositoryImpl : UserRepository {
    private val userDB = HashMap<String, User>()
    private val listingDB = HashMap<String, Listing>()

    init {
        userDB[MockAuthRepositoryImpl.loggedInUser.id] = MockAuthRepositoryImpl.loggedInUser
    }

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
                val updatedFavoriteListings =
                    user.favoriteListings.toMutableMap().apply { put(favListingId, true) }
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
        favListingId: String
    ): Flow<ApiResponse<User>> {
        return flow {
            emit(ApiResponse.Loading)
            delay(1000)
            if (userDB.containsKey(userId)) {
                val user = userDB[userId]!!
                val updatedFavoriteListings =
                    user.favoriteListings.toMutableMap().apply { remove(favListingId) }
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
            delay(1000)
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
}
