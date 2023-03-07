package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow


/**
 * Use case for updating a listing in a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 */
class UpdateListing(private val repository: ListingRepository) {

    /**
     * Updates the listing with ID [listingId] in the [repository]
     *
     * The ID of the given [updatedListing] shall be discarded
     *
     * @param [listingId] the ID of the listing to update
     * @param [updatedListing] the listing containing all the updated fields - the ID of this listing will be discarded
     */
    suspend operator fun invoke(listingId: Long, updatedListing: Listing): Flow<ApiResponse<Listing>> {
        return repository.updateListing(listingId, updatedListing)
    }
}

