package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for removing a listing from a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 */
class RemoveListing(private val repository: ListingRepository) {

    /**
     * Removes the [listing] from the [repository]
     *
     * @param [listing] the listing to remove
     */
    suspend operator fun invoke(listing: Listing): Flow<ApiResponse<Boolean>> {
        return repository.removeListing(listing.id)
    }


    /**
     * Removes the listing matching the given [listingId] from the [repository]
     *
     * @param [listingId] the ID of the listing to remove
     */
    suspend operator fun invoke(listingId: String): Flow<ApiResponse<Boolean>> {
        return repository.removeListing(listingId)
    }
}
