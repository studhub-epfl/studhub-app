package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(user: User): Flow<ApiResponse<User>>
    suspend fun getUser(userId: String): Flow<ApiResponse<User>>
    suspend fun updateUserInfo(userId: String, updatedUser: User): Flow<ApiResponse<User>>
    suspend fun removeUser(userId: String): Flow<ApiResponse<Boolean>>
    suspend fun addFavoriteListing(
        userId: String,
        favListingId: String
    ): Flow<ApiResponse<User>>

    suspend fun removeFavoriteListing(
        userId: String,
        favListingId: String
    ): Flow<ApiResponse<User>>

    suspend fun getFavoriteListings(userId: String): Flow<ApiResponse<List<Listing>>>
    suspend fun blockUser(userId: String, blockedUserId: String): Flow<ApiResponse<User>>
    suspend fun unblockUser(userId: String, blockedUserId: String): Flow<ApiResponse<User>>
}
