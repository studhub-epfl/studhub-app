package com.studhub.app.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl : UserRepository {
    private val db: DatabaseReference = Firebase.database.reference
    private lateinit var storage: FirebaseStorage

    override suspend fun createUser(user: User): Flow<ApiResponse<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(userId: Long): Flow<ApiResponse<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(userId: Long, updatedUser: User): Flow<ApiResponse<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun removeUser(userId: Long): Flow<ApiResponse<Boolean>> {
        TODO("Not yet implemented")
    }
}
