package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for creating a listing in a given [repository]
 *
 * @param [repository] the which the use case will act on
 */
class CreateListing @Inject constructor(private val repository: ListingRepository) {

    /**
     * Adds the given [listing] to a [repository]
     *
     * @param [listing] the listing to create
     */
    suspend operator fun invoke(listing: Listing): Flow<ApiResponse<Listing>> {
        return repository.createListing(listing)
    }
}
