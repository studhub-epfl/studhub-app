package com.studhub.app.domain.usecase.category

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting a category from a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 */
class GetCategory @Inject constructor(private val repository: CategoryRepository) {

    /**
     * Retrieves the category matching the given [listingId] from the [repository]
     *
     * @param [listingId] the ID of the listing to retrieve
     */
    suspend operator fun invoke(categoryId: String): Flow<ApiResponse<Category>> {
        return repository.getCategory(categoryId)
    }
}
