package com.studhub.app

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ListingRepository
import com.studhub.app.domain.usecase.listing.GetListings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.assertEquals
import kotlin.random.Random

class ListingUseCaseTest {
    private val listingDB = HashMap<Long, Listing>()

    private var repository: ListingRepository = object : ListingRepository {

        override suspend fun createListing(listing: Listing): Flow<ApiResponse<Boolean>> {
            return flow {
                listingDB[listing.id] = listing
                emit(ApiResponse.Success(true))
            }
        }

        override suspend fun getListings(): Flow<ApiResponse<List<Listing>>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                emit(ApiResponse.Success(listingDB.values.toList()))
            }
        }

        override suspend fun getListing(listingId: Long): Flow<ApiResponse<Listing>> {
            return flow {
                if (listingDB.containsKey(listingId))
                    emit(ApiResponse.Success(listingDB.getValue(listingId)))
                else
                    emit(ApiResponse.Failure("No entry for this key"))
            }
        }

        override suspend fun updateListing(listingId: Long, updatedListing: Listing): Flow<ApiResponse<Listing>> {
            TODO("Not yet implemented")
        }

        override suspend fun removeListing(listing: Listing): Flow<ApiResponse<Boolean>> {
            TODO("Not yet implemented")
        }

        override suspend fun removeListing(listingId: Long): Flow<ApiResponse<Boolean>> {
            TODO("Not yet implemented")
        }
    }

    @Test
    fun `Get Listings Use Case retrieves all entries of the given repository`() = runBlocking {
        val getListings = GetListings(repository)

        val random = Random.nextLong()
        listingDB[1L] = Listing(id = 1L, name = "Product 1", seller = User())
        listingDB[random] = Listing(id = random, name = "Product 8471", seller = User())

        getListings().collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val data = response.data
                    assertEquals(listingDB.count(), data.count())
                    assertEquals("Product 8471", data.first { it.id == random }.name)
                }
                is ApiResponse.Failure -> assertEquals(true, false)
                is ApiResponse.Loading -> {}
            }
        }

        // reset DB
        listingDB.clear()
    }
}