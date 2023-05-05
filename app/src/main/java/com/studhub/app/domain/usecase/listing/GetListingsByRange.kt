package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject


/**
 * Use case for getting all listings from a given [repository] matching a double searching constraint on price
 *
 * @param [repository] the repository which the use case will act on
 */
class GetListingsByRange @Inject constructor(private val repository: ListingRepository) {

    /**
     * Retrieves all listings matching the given price from [keyword1] until [keyword2] from the [repository]
     *
     * @param [keyword1] the first numerical value to compare to the listings
     * @param [keyword2] the second numerical value to compare to the listings
     */
    suspend operator fun invoke(keyword1: String, keyword2: String): Flow<ApiResponse<List<Listing>>> {
        if (keyword1.toFloatOrNull() == null || keyword2.toFloatOrNull() == null) {
            return flowOf(ApiResponse.Failure("Input is not a number"))
        }

        return repository.getListingsByRange(keyword1,keyword2)
    }
}
