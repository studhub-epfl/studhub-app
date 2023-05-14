package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategories(): Flow<ApiResponse<List<Category>>>
    suspend fun getCategory(categoryId: String): Flow<ApiResponse<Category>>

    suspend fun getSubCategory(categoryId: String): Flow<ApiResponse<Category>>
    suspend fun getSubCategories(): Flow<ApiResponse<List<Category>>>
}
