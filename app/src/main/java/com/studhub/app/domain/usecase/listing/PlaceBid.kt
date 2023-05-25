package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaceBid @Inject constructor(
    private val repository: ListingRepository,
    private val authRepository: AuthRepository
) {

    /**
     * Update the listing with a newly placed bid
     *
     * The ID of the given [updatedListing] shall be discarded
     *
     * @param [listingId] the ID of the listing to update
     * @param [updatedListing] the listing containing all the updated fields - the ID of this listing will be discarded
     */
    suspend operator fun invoke(
        listing: Listing,
        bid: Float
    ): Flow<ApiResponse<Listing>> {
        val newListing = listing.copy(currentBidderId = authRepository.currentUserUid, price = bid)
        return repository.updateListing(listing.id, newListing)
    }
}
