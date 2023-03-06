package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow

class UpdateListing(private val repository: ListingRepository) {
    suspend operator fun invoke(listingId: Long, updatedListing: Listing): Flow<ApiResponse<Listing>> {
        return repository.updateListing(listingId, updatedListing)
    }
}
