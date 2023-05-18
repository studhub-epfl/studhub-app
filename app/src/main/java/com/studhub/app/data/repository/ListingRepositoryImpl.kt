package com.studhub.app.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.network.NetworkStatus
import com.studhub.app.data.storage.StorageHelper
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.ListingType
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListingRepositoryImpl @Inject constructor(
    private val remoteDb: FirebaseDatabase,
    private val localDb: LocalDataSource,
    private val networkStatus: NetworkStatus
) : ListingRepository {

    private val db = remoteDb.getReference("listings")
    private var provisionalListing = mutableListOf<Listing>()
    private val storageHelper = StorageHelper()


    override suspend fun createListing(listing: Listing): Flow<ApiResponse<Listing>> {
        val listingId: String = db.push().key.orEmpty()

        return flow {
            emit(ApiResponse.Loading)

            if (!networkStatus.isConnected) {
                emit(ApiResponse.NO_INTERNET_CONNECTION)
                return@flow
            }

            // store pictures
            val listingToPush = listing.copy(
                id = listingId,
                picturesUri = null,
                pictures = listing.picturesUri?.map {
                    storageHelper.storePicture(it, "listings")
                } ?: emptyList())

            val query = db.child(listingId).setValue(listingToPush)

            query.await()

            if (query.isSuccessful) {
                emit(ApiResponse.Success(listingToPush))
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }
    }

    override suspend fun getListings(): Flow<ApiResponse<List<Listing>>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            emit(ApiResponse.NO_INTERNET_CONNECTION)
            return@flow
        }

        val query = db.get()

        query.await()

        if (query.isSuccessful) {
            val listings = mutableListOf<Listing>()

            //val retrievedListing: Listing? = query.result.getValue(Listing::class.java)
            for (listingSnapshot in query.result.children) {
                val retrievedListing: Listing? = listingSnapshot.getValue(Listing::class.java)
                if (retrievedListing != null) {
                    listings.add(retrievedListing)
                }
            }

            emit(ApiResponse.Success(listings))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

    override suspend fun getListing(listingId: String): Flow<ApiResponse<Listing>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            emit(ApiResponse.NO_INTERNET_CONNECTION)
            return@flow
        }

        val query = db.child(listingId).get()

        query.await()

        if (query.isSuccessful) {
            val retrievedListing: Listing? = query.result.getValue(Listing::class.java)
            if (retrievedListing == null) {
                emit(ApiResponse.Failure("Listing does not exist"))
            } else {
                emit(ApiResponse.Success(retrievedListing))
            }
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

    override suspend fun getListingsBySearch(
        keyword: String,
        minPrice: String,
        maxPrice: String,
        blockedUsers: Map<String, Boolean>
    ): Flow<ApiResponse<List<Listing>>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            emit(ApiResponse.NO_INTERNET_CONNECTION)
            return@flow
        }

        val query = db.get()

        query.await()

        if (query.isSuccessful) {
            val listings = mutableListOf<Listing>()

            query.result.children.forEach { snapshot ->
                val listing = snapshot.getValue(Listing::class.java)
                if (listing != null && (blockedUsers[listing.seller.id] != true) &&
                    (listing.name.contains(keyword, true) || listing.description.contains(
                        keyword,
                        true
                    )
                            || listing.price.toString().contains(keyword, true))
                    && listing.price >= minPrice.toFloat()
                    && listing.price <= maxPrice.toFloat()
                ) {
                    listings.add(listing)

                }


            }
            provisionalListing = listings
            emit(ApiResponse.Success(provisionalListing))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }



    override suspend fun updateListing(
        listingId: String,
        updatedListing: Listing
    ): Flow<ApiResponse<Listing>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            emit(ApiResponse.NO_INTERNET_CONNECTION)
            return@flow
        }

        val listingToPush = updatedListing.copy(id = listingId)
        // set the new value of the Listing on the database
        val query = db.child(listingId).setValue(listingToPush)

        query.await()

        if (query.isSuccessful) {
            emit(ApiResponse.Success(listingToPush))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }

    }

    override suspend fun removeListing(listingId: String): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            emit(ApiResponse.NO_INTERNET_CONNECTION)
            return@flow
        }

        // remove the old value on the database
        val query = db.child(listingId).removeValue()

        query.await()

        if (query.isSuccessful) {
            emit(ApiResponse.Success(true))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }

    }

    override suspend fun updateListingToBidding(
        listing: Listing,
        startingPrice: Float,
        deadline: Date
    ): Flow<ApiResponse<Listing>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            emit(ApiResponse.NO_INTERNET_CONNECTION)
            return@flow
        }
        val biddingListing = listing.copy(
            price = startingPrice,
            type = ListingType.BIDDING,
            biddingDeadline = deadline
        )
        val query = db.child(listing.id).setValue(biddingListing)

        query.await()

        if (query.isSuccessful) {
            emit(ApiResponse.Success(biddingListing))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

}
