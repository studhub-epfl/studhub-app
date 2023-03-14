package com.studhub.app.data.repository

import com.google.android.gms.common.api.Api
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ListingRepositoryImpl : ListingRepository {

    private val db = Firebase.database.getReference("listings")

    override suspend fun createListing(listing: Listing): Flow<ApiResponse<Listing>> {
        val listingId: String = db.push().key.orEmpty()
        val listingToPush: Listing = listing.copy(id = listingId)

        return flow {
            emit(ApiResponse.Loading)

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

        val query = db.get()

        query.await()

        if (query.isSuccessful) {
            val listings = mutableListOf<Listing>()

            //val retrievedListing: Listing? = query.result.getValue(Listing::class.java)
            for (listingSnapshot in query.result.children){
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

    override suspend fun updateListing(
        listingId: String,
        updatedListing: Listing
    ): Flow<ApiResponse<Listing>> {
        TODO("Not yet implemented")
    }

    override suspend fun removeListing(listingId: String): Flow<ApiResponse<Boolean>> {
        TODO("Not yet implemented")
    }
}