package com.studhub.app.data.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.ListingType
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.util.*
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class MockListingRepositoryImpl : ListingRepository {
    val listingDB = HashMap<String, Listing>()
    val localListingCache = HashMap<String, Listing>()

    override suspend fun createListing(listing: Listing): Flow<ApiResponse<Listing>> {
        return flow {
            val id = listing.id.ifBlank { Random.nextLong().toString() }
            val pushed = listing.copy(id = id)
            listingDB[id] = pushed
            emit(ApiResponse.Success(pushed))
        }
    }

    override suspend fun saveDraftListing(listing: Listing): Flow<ApiResponse<Listing>> {
        val id = listing.id.ifBlank { Random.nextLong().toString() }
        val pushed = listing.copy(id = id)
        localListingCache[id] = pushed
        return flowOf(ApiResponse.Success(pushed))
    }

    override suspend fun getDraftListing(listingId: String): Flow<ApiResponse<Listing?>> {
        return flowOf(ApiResponse.Success(localListingCache[listingId]))
    }

    override suspend fun getListings(): Flow<ApiResponse<List<Listing>>> {
        return flow {
            emit(ApiResponse.Success(listingDB.values.toList()))
        }
    }

    override suspend fun getListing(listingId: String): Flow<ApiResponse<Listing>> {
        return flow {
            if (listingDB.containsKey(listingId))
                emit(ApiResponse.Success(listingDB.getValue(listingId)))
            else
                emit(ApiResponse.Failure("No entry for this key"))
        }
    }

    override suspend fun getUserListings(user: User): Flow<ApiResponse<List<Listing>>> {
        return flow {
            emit(ApiResponse.Success(listingDB.values.toList()))
        }
    }

    override suspend fun getUserDraftListings(user: User): Flow<ApiResponse<List<Listing>>> {
        TODO("Not yet implemented")
    }


    override suspend fun getListingsBySearch(
        keyword: String,
        keyword1: String,
        keyword2: String,
        blockedUsers: Map<String, Boolean>
    ): Flow<ApiResponse<List<Listing>>> {
        return flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Success(listingDB.values.filter { k ->
                (k.description.compareTo(keyword) == 0 || k.name.compareTo(keyword) == 0)
                        && blockedUsers[k.seller.id] != true
            }))
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

    override suspend fun updateListingToBidding(
        listing: Listing,
        startingPrice: Float,
        deadline: Date
    ): Flow<ApiResponse<Listing>> {
        return flow {
            emit(ApiResponse.Loading)
            val biddingListing = listing.copy(
                price = startingPrice,
                type = ListingType.BIDDING,
                biddingDeadline = deadline
            )
            listingDB[listing.id] = biddingListing
            emit(ApiResponse.Success(listingDB.getValue(listing.id)))
        }
    }
}
