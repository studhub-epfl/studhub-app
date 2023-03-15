package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import kotlinx.coroutines.flow.Flow

interface ListingRepository {
    suspend fun createListing(listing: Listing): Flow<ApiResponse<Listing>>

    suspend fun getListings(): Flow<ApiResponse<List<Listing>>>
    suspend fun getListing(listingId: String): Flow<ApiResponse<Listing>>

    suspend fun updateListing(listingId: String, updatedListing: Listing): Flow<ApiResponse<Listing>>

    suspend fun removeListing(listingId: String): Flow<ApiResponse<Boolean>>
}
