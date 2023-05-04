package com.studhub.app.data.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Singleton

@Singleton
class MockUserRepositoryImpl : UserRepository {
    private val userDB = HashMap<String, User>()
    private val listingDB = HashMap<String, Listing>()
    private val ratings = mutableMapOf<String, MutableMap<String, Rating>>()

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
        favListingId: String
    ): Flow<ApiResponse<User>> {
        return flow {
            emit(ApiResponse.Loading)
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

    override suspend fun blockUser(userId: String, blockedUserId: String): Flow<ApiResponse<User>> {
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

    override suspend fun addRating(userId: String, rating: Rating): Flow<ApiResponse<Rating>> = flow {
        val userRatings = ratings.getOrPut(userId) { mutableMapOf() }
        val ratingId = UUID.randomUUID().toString()
        val ratingToAdd = rating.copy(id = ratingId)
        userRatings[ratingId] = ratingToAdd

        emit(ApiResponse.Success(ratingToAdd))
    }

    override suspend fun updateRating(userId: String, ratingId: String, rating: Rating): Flow<ApiResponse<Rating>> = flow {
        val userRatings = ratings[userId]
        if (userRatings != null && userRatings.containsKey(ratingId)) {
            userRatings[ratingId] = rating
            emit(ApiResponse.Success(rating))
        } else {
            emit(ApiResponse.Failure("Rating not found"))
        }
    }

    override suspend fun deleteRating(userId: String, ratingId: String): Flow<ApiResponse<Boolean>> = flow {
        val userRatings = ratings[userId]
        if (userRatings != null && userRatings.containsKey(ratingId)) {
            userRatings.remove(ratingId)
            emit(ApiResponse.Success(true))
        } else {
            emit(ApiResponse.Failure("Rating not found"))
        }
    }

    override suspend fun getUserRatings(userId: String): Flow<ApiResponse<List<Rating>>> = flow {
        val userRatings = ratings[userId]
        if (userRatings != null) {
            emit(ApiResponse.Success(userRatings.values.toList()))
        } else {
            emit(ApiResponse.Success(emptyList()))
        }
    }
}


