package com.studhub.app.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class ListingRepositoryImpl : ListingRepository {
    //private firebaseDB..
    private val db = FirebaseDatabase.getInstance().reference

    override suspend fun createListing(listing: Listing): Flow<ApiResponse<Boolean>> {
        return flow {
            emit(ApiResponse.Loading)
            //var list = db.setValue(listing)

            //emit(ApiResponse.Success(true))
            //if emit..Success..
            //else emit.. Failure..

            db.setValue(listing).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                   ApiResponse.Success(true)
                    // Operazione completata con successo
                } else {
                    ApiResponse.Failure("Error during Operation")
                    // Si è verificato un errore durante l'operazione
                }
            }
            //(firebase: id..)


        }
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