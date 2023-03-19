package com.studhub.app.presentation.ui.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

/***
 * Created to test clicking on a listing thumbnail
 * using generated (fake) listings.
 */
class FakeListingRepository : ListingRepository {
    override suspend fun createListing(listing: Listing): Flow<ApiResponse<Listing>> {
        TODO("Not yet implemented")
    }

    override suspend fun getListings(): Flow<ApiResponse<List<Listing>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getListing(id: String): Flow<ApiResponse<Listing>> {
        // Simulate a delay in network response
        delay(1000)

        // Generate a random Listing object for testing purposes
        val listing = Listing(
            id = id,
            name = "Test Listing $id",
            description = "This is a test listing generated by FakeListingRepository.",
            categories = listOf(Category(name = "Test Category")),
            seller = User(userName = "test_user"),
            price = Random.nextFloat() * 1000
        )

        // Return the generated listing as a success response
        return flow { emit(ApiResponse.Success(listing)) }
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
