package com.studhub.app.data.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow

class ListingRepositoryImpl : ListingRepository {
    override suspend fun createListing(listing: Listing): Flow<ApiResponse<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun getListings(): Flow<ApiResponse<List<Listing>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getListing(listingId: Long): Flow<ApiResponse<Listing>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateListing(
        listingId: Long,
        updatedListing: Listing
    ): Flow<ApiResponse<Listing>> {
        TODO("Not yet implemented")
    }

    override suspend fun removeListing(listingId: Long): Flow<ApiResponse<Boolean>> {
        TODO("Not yet implemented")
    }
}