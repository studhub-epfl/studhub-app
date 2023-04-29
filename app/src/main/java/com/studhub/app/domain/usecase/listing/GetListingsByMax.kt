package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject


/**
 * Use case for getting all listings from a given [repository] matching a searching constraint
 *
 * @param [repository] the repository which the use case will act on
 */
class GetListingsByMax @Inject constructor(private val repository: ListingRepository) {

    /**
     * Retrieves all listings matching the given [keyword] from the [repository]
     *
     * @param [keyword] the value to compare to the listings
     */
    suspend operator fun invoke(keyword: String): Flow<ApiResponse<List<Listing>>> {
        if (keyword.length < 3) {
            return flowOf(ApiResponse.Failure("Too few characters"))
        }

        return repository.getListingsByMax(keyword)
    }
}
