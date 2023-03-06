package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow

class GetListings constructor(private val repository: ListingRepository) {
    suspend operator fun invoke(): Flow<ApiResponse<List<Listing>>> {
        return repository.getListings()
    }
}
