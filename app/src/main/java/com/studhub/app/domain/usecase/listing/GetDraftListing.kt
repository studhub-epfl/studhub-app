package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for getting a draft listing from a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 */
class GetDraftListing @Inject constructor(private val repository: ListingRepository) {

    /**
     * Retrieves the draft listing matching the given [listingId] from the [repository]
     *
     * @param [listingId] the ID of the draft listing to retrieve
     */
    suspend operator fun invoke(listingId: String): Flow<ApiResponse<Listing?>> {
        return repository.getDraftListing(listingId)
    }
}
