package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(user: User): Flow<ApiResponse<User>>
    suspend fun getUser(userId: String): Flow<ApiResponse<User>>
    suspend fun updateUser(userId: String, updatedUser: User): Flow<ApiResponse<User>>
    suspend fun removeUser(userId: String): Flow<ApiResponse<Boolean>>
}
