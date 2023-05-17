package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(user: User): Flow<ApiResponse<User>>
    suspend fun getUser(userId: String): Flow<ApiResponse<User>>
    suspend fun updateUserInfo(userId: String, updatedUser: User): Flow<ApiResponse<User>>
    suspend fun removeUser(userId: String): Flow<ApiResponse<Boolean>>
    suspend fun addFavoriteListing(
        userId: String,
        favListing: Listing
    ): Flow<ApiResponse<User>>

    suspend fun removeFavoriteListing(
        userId: String,
        favListing: Listing
    ): Flow<ApiResponse<User>>

    suspend fun getFavoriteListings(userId: String): Flow<ApiResponse<List<Listing>>>
    suspend fun blockUser(userId: String, blockedUserId: String): Flow<ApiResponse<User>>
    suspend fun unblockUser(userId: String, blockedUserId: String): Flow<ApiResponse<User>>
    suspend fun addRating(userId: String, rating: Rating): Flow<ApiResponse<Rating>>
    suspend fun updateRating(userId: String, ratingId: String, rating: Rating): Flow<ApiResponse<Rating>>
    suspend fun deleteRating(userId: String, ratingId: String): Flow<ApiResponse<Boolean>>
    suspend fun getUserRatings(userId: String): Flow<ApiResponse<List<Rating>>>
}
