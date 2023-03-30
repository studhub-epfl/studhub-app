package com.studhub.app.data.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@Singleton
class MockListingRepositoryImpl: ListingRepository {
    private val listingDB = HashMap<String, Listing>()

    override suspend fun createListing(listing: Listing): Flow<ApiResponse<Listing>> {
        return flow {
            emit(ApiResponse.Loading)
            listingDB[listing.id] = listing
            emit(ApiResponse.Success(listing))
        }
    }

    override suspend fun getListings(): Flow<ApiResponse<List<Listing>>> {
        return flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Success(listingDB.values.toList()))
        }
    }

    override suspend fun getListing(listingId: String): Flow<ApiResponse<Listing>> {
        return flow {
            emit(ApiResponse.Loading)
            if (listingDB.containsKey(listingId))
                emit(ApiResponse.Success(listingDB.getValue(listingId)))
            else
                emit(ApiResponse.Failure("No entry for this key"))
        }
    }

    override suspend fun getListingsBySearch(keyword: String): Flow<ApiResponse<List<Listing>>> {
        return flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Success(listingDB.values.filter { k-> (k.description.compareTo(keyword)==0 || k.name.compareTo(keyword) == 0) }))
        }
    }

    override suspend fun updateListing(
        listingId: String,
        updatedListing: Listing
    ): Flow<ApiResponse<Listing>> {
        return flow {
            emit(ApiResponse.Loading)
            if (listingDB.containsKey(listingId)) {
                val newListing = updatedListing.copy(id = listingId)
                listingDB[listingId] = newListing
                emit(ApiResponse.Success(listingDB.getValue(listingId)))
            } else
                emit(ApiResponse.Failure("No entry for this key"))
        }
    }

    override suspend fun removeListing(listingId: String): Flow<ApiResponse<Boolean>> {
        return flow {
            emit(ApiResponse.Loading)
            listingDB.remove(listingId)
            emit(ApiResponse.Success(true))
        }
    }
}
