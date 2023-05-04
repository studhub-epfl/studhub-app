package com.studhub.app.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import com.studhub.app.listing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.function.Predicate
import javax.inject.Singleton

@Singleton
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

    override suspend fun getListingsBySearch(keyword: String): Flow<ApiResponse<List<Listing>>> = flow {
        emit(ApiResponse.Loading)
        val query = db.get()


        query.await()

        if (query.isSuccessful) {
            val listings = mutableListOf<Listing>()

            query.result.children.forEach { snapshot ->
                val listing = snapshot.getValue(Listing::class.java)
                if (listing != null && (listing.name.contains(keyword) || listing.description.contains(keyword)
                            || listing.price.toString().contains(keyword))) {
                    listings.add(listing)

                }

                if(listing != null && keyword.contains('-')) {

                    if(listing.price >= keyword.substringBefore('-').toFloat()
                        && listing.price <= keyword.substringAfter('-').toFloat()){
                        listings.add(listing)
                    }


                }
            }


            emit(ApiResponse.Success(listings))
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

}
