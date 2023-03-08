package com.studhub.app.domain.usecase.category

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetCategory(private val repository: CategoryRepository) {
    suspend operator fun invoke(categoryId: Long): Flow<ApiResponse<Category>> {
        return repository.getCategory(categoryId)
    }
}