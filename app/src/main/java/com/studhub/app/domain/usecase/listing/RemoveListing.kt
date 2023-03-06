package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow

class RemoveListing(private val repository: ListingRepository) {
    suspend operator fun invoke(listing: Listing): Flow<ApiResponse<Boolean>> {
        return repository.removeListing(listing)
    }

    suspend operator fun invoke(listingId: Long): Flow<ApiResponse<Boolean>> {
        return repository.removeListing(listingId)
    }
}