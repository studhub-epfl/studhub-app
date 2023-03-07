package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import kotlinx.coroutines.flow.Flow

interface ListingRepository {
    suspend fun createListing(listing: Listing): Flow<ApiResponse<Boolean>>

    suspend fun getListings(): Flow<ApiResponse<List<Listing>>>
    suspend fun getListing(listingId: Long): Flow<ApiResponse<Listing>>

    suspend fun updateListing(listingId: Long, updatedListing: Listing): Flow<ApiResponse<Listing>>

    suspend fun removeListing(listingId: Long): Flow<ApiResponse<Boolean>>
}
