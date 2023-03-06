package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetListing(private val repository: ListingRepository) {
    suspend operator fun invoke(listingId: Long): Flow<ApiResponse<Listing>> {
        return repository.getListing(listingId)
    }
}