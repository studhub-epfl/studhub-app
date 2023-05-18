package com.studhub.app.domain.usecase.category


import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all categories from a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 */
class GetCategories @Inject constructor(private val repository: CategoryRepository) {
    /**
     * Retrieves all categories from the [repository]
     */
    suspend operator fun invoke(): Flow<ApiResponse<List<Category>>> {
        return repository.getCategories()
    }
}
