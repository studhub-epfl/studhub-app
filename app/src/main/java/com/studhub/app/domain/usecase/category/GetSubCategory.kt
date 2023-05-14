package com.studhub.app.domain.usecase.category

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSubCategory @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(categoryId: String): Flow<ApiResponse<Category>> {
        return repository.getSubCategory(categoryId)
    }
}