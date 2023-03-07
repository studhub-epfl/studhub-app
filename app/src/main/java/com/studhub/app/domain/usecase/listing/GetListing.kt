package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for getting a listing from a given [repository]
 *
 * @param ListingRepository the [repository] which the use case will act on
 */
class GetListing(private val repository: ListingRepository) {

    /**
     * Retrieves the listing matching the given [listingId] from the [repository]
     *
     * @param Long the [listingId] of the listing to retrieve
     */
    suspend operator fun invoke(listingId: Long): Flow<ApiResponse<Listing>> {
        return repository.getListing(listingId)
    }
}
