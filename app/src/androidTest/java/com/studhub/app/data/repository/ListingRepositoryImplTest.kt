package com.studhub.app.data.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.usecase.listing.CreateListing
import com.studhub.app.domain.usecase.listing.GetListing
import com.studhub.app.domain.usecase.listing.GetListings
import kotlinx.coroutines.runBlocking
import org.junit.Assert.fail
import org.junit.Test
import kotlin.random.Random

class ListingRepositoryImplTest {
    private val repository = ListingRepositoryImpl()

    @Test
    fun setAndGetSameListing() {
        val createListing = CreateListing(repository)
        val getListing = GetListing(repository)

        lateinit var listing: Listing

        runBlocking {

            val product = Listing(
                id = Random.nextLong().toString(),
                name = "Product ${Random.nextLong()}",
            )

            createListing(product).collect {
                when (it) {
                    is ApiResponse.Success -> listing = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }

        runBlocking {
            getListing(listing.id).collect {
                when (it) {
                    is ApiResponse.Success -> assert(it.data == listing)
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }

        }
    }

    @Test
    fun getListingsShouldNotFail() {
        val getListings = GetListings(repository)

        runBlocking {
            getListings().collect {
                when (it) {
                    is ApiResponse.Success -> assert(true)
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }

        }

    }

}
