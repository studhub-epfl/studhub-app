package com.studhub.app.domain.usecase

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ListingRepository
import com.studhub.app.domain.usecase.listing.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

import kotlin.random.Random

class ListingUseCaseTest {
    private val listingDB = HashMap<String, Listing>()

    private val repository: ListingRepository = object : ListingRepository {

        override suspend fun createListing(listing: Listing): Flow<ApiResponse<Listing>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                listingDB[listing.id] = listing
                emit(ApiResponse.Success(listing))
            }
        }

        override suspend fun getListings(): Flow<ApiResponse<List<Listing>>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                emit(ApiResponse.Success(listingDB.values.toList()))
            }
        }

        override suspend fun getListing(listingId: String): Flow<ApiResponse<Listing>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
                if (listingDB.containsKey(listingId))
                    emit(ApiResponse.Success(listingDB.getValue(listingId)))
                else
                    emit(ApiResponse.Failure("No entry for this key"))
            }
        }

        override suspend fun getListingsBySearch(keyword: String): Flow<ApiResponse<List<Listing>>> {
            TODO("Not yet implemented")
        }

        override suspend fun updateListing(
            listingId: String,
            updatedListing: Listing
        ): Flow<ApiResponse<Listing>> {
            return flow {
                emit(ApiResponse.Loading)
                delay(1000)
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
                delay(1000)
                listingDB.remove(listingId)
                emit(ApiResponse.Success(true))
            }
        }
    }

    @After
    fun clearDB() {
        listingDB.clear()
    }

    @Test
    fun testGetListingsRetrievesAllEntriesOfGivenRepository() = runBlocking {
        val getListings = GetListings(repository)

        val productId = Random.nextLong().toString()
        val productName = Random.nextLong().toString()
        listingDB["1"] = Listing(id = "1", name = "Product 1", seller = User())
        listingDB[productId] = Listing(id = productId, name = productName, seller = User())

        getListings().collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val data = response.data
                    assertEquals(listingDB.count(), data.count())
                    assertEquals(productName, data.first { it.id == productId }.name)
                }
                is ApiResponse.Failure -> fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun getListingUseCaseRetrievesCorrectEntry() = runBlocking {
        val getListing = GetListing(repository)

        val productId = Random.nextLong().toString()
        val productName = Random.nextLong().toString()
        listingDB[productId] = Listing(id = productId, name = productName, seller = User())

        getListing(productId).collect { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val listing = response.data
                    assertNotNull(listing)
                    assertEquals(productName, listing.name)
                }
                is ApiResponse.Failure -> fail("Request failure")
                is ApiResponse.Loading -> {}
            }
        }
    }

    @Test
    fun createListingUseCaseCreatesCorrectEntry() =
        runBlocking {
            val createListing = CreateListing(repository)

            val productId = Random.nextLong().toString()
            val productName = Random.nextLong().toString()
            val listing = Listing(id = productId, name = productName, seller = User())

            createListing(listing).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        val result = response.data
                        assertNotNull(result)
                        assertEquals(productName, listingDB.getValue(productId).name)
                    }
                    is ApiResponse.Failure -> fail("Request failure")
                    is ApiResponse.Loading -> {}
                }
            }
        }

    @Test
    fun updateListingUseCaseCorrectlyUpdatesEntry() =
        runBlocking {
            val updateListing = UpdateListing(repository)

            val productId1 = Random.nextLong().toString()
            val productName1 = Random.nextLong().toString()
            val productId2 = Random.nextLong().toString()
            val productName2 = Random.nextLong().toString()
            listingDB[productId1] = Listing(id = productId1, name = productName1, seller = User())

            val updatedProduct = Listing(id = productId2, name = productName2, seller = User())

            updateListing(productId1, updatedProduct).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        val result: Listing = response.data
                        val expectedResult: Listing = updatedProduct.copy(id = productId1)
                        val product1FromDB: Listing = listingDB.getValue(productId1)

                        assertEquals(
                            "returned listing should match the updated one",
                            result,
                            expectedResult
                        )

                        assertEquals("name should be updated", productName2, product1FromDB.name)

                        assertEquals("id should not be updated", productId1, product1FromDB.id)
                    }
                    is ApiResponse.Failure -> fail("Request failure")
                    is ApiResponse.Loading -> {}
                }
            }
        }

    @Test
    fun removeListingUseCaseCorrectlyRemovesEntry() =
        runBlocking {
            val removeListing = RemoveListing(repository)

            val productId = Random.nextLong().toString()
            val productName = Random.nextLong().toString()
            val listing = Listing(id = productId, name = productName, seller = User())
            listingDB[productId] = listing

            removeListing(listing).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        val result = response.data
                        assertTrue(result)
                        assertNull(listingDB[productId])
                    }
                    is ApiResponse.Failure -> fail("Request failure")
                    is ApiResponse.Loading -> {}
                }
            }
        }

    @Test
    fun removeListingUseCaseCorrectlyRemovesEntryWithGivenId() =
        runBlocking {
            val removeListing = RemoveListing(repository)

            val productId = Random.nextLong().toString()
            val productName = Random.nextLong().toString()
            val listing = Listing(id = productId, name = productName, seller = User())
            listingDB[productId] = listing

            removeListing(productId).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        val result = response.data
                        assertTrue(result)
                        assertNull(listingDB[productId])
                    }
                    is ApiResponse.Failure -> fail("Request failure")
                    is ApiResponse.Loading -> {}
                }
            }
        }
}
