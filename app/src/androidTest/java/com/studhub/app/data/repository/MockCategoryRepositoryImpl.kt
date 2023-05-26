package com.studhub.app.data.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockCategoryRepositoryImpl : CategoryRepository {
    val categories = HashMap<String, Category>()

    override suspend fun getCategories(): Flow<ApiResponse<List<Category>>> =
        flowOf(ApiResponse.Success(categories.values.toList()))

    override suspend fun getCategory(categoryId: String): Flow<ApiResponse<Category>> =
        if (categories.containsKey(categoryId))
            flowOf(ApiResponse.Success(categories[categoryId]!!))
        else
            flowOf(ApiResponse.Failure("No such key"))
}
