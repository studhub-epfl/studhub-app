package com.studhub.app.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.CategoryRepository
import com.studhub.app.domain.repository.ListingRepository
import com.studhub.app.domain.usecase.listing.CreateListing
import com.studhub.app.domain.usecase.listing.GetListing
import com.studhub.app.domain.usecase.listing.GetListings
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.random.Random

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ListingRepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var listingRepo: ListingRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun setAndGetSameListing() {

        lateinit var listing: Listing

        runBlocking {

            val product = Listing(
                id = Random.nextLong().toString(),
                name = "Product ${Random.nextLong()}",
            )

            listingRepo.createListing(product).collect {
                when (it) {
                    is ApiResponse.Success -> listing = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }

        runBlocking {
            listingRepo.getListing(listing.id).collect {
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
        runBlocking {
            listingRepo.getListings().collect {
                when (it) {
                    is ApiResponse.Success -> assert(true)
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }

    @Test
    fun updateListingShouldNotFail() {
        // stub test
        val fakeListing = Listing(id = "000", name = "My listing")

        runBlocking {
            listingRepo.updateListing(fakeListing.id, fakeListing).collect {
                when (it) {
                    is ApiResponse.Failure -> fail()
                    else -> assert(true)
                }
            }
        }
    }

    @Test
    fun removeListingShouldNotFail() {
        // stub test
        val fakeListing = Listing(id = "000", name = "My listing")

        runBlocking {
            listingRepo.removeListing(fakeListing.id).collect {
                when (it) {
                    is ApiResponse.Failure -> fail()
                    else -> assert(true)
                }
            }
        }
    }

}
