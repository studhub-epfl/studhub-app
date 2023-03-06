package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(user: User): Flow<ApiResponse<Boolean>>
    suspend fun getUser(userId: Long): Flow<ApiResponse<User>>
    suspend fun updateUser(userId: Long, user: User): Flow<ApiResponse<User>>
    suspend fun removeUser(userId: Long): Flow<ApiResponse<Boolean>>
}
