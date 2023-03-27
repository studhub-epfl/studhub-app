package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * Use case for getting all listings from a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 */
class GetListingsBySearch @Inject constructor(private val repository: ListingRepository) {

    /**
     * Retrieves all listings from the [repository]
     */
    suspend operator fun invoke(keyword: String): Flow<ApiResponse<List<Listing>>> {
        return repository.getListingsBySearch(keyword)
    }
}
