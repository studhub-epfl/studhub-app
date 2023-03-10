package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategories(): Flow<ApiResponse<List<Category>>>
    suspend fun getCategory(categoryId: Long): Flow<ApiResponse<Category>>
}
