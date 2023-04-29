package com.studhub.app.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ListingRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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

    // @Inject
    //  lateinit var getListingsBySearch: GetListingsBySearch

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

    @Test
    fun getListingsBySearchShouldNotFailOnDescription() {
        lateinit var listing: Listing
        lateinit var listing2: Listing
        lateinit var listing3: Listing
        lateinit var listing4: Listing
        runBlocking {

            val product = Listing(
                description = Random.nextLong().toString(),
                name = "Product ${Random.nextLong()}",
            )
            val product2 = Listing(
                description = Random.nextLong().toString(),
                name = "Product ${Random.nextLong()}",
            )
            val product3 = Listing(
                description = "a key",
                name = "Product ${Random.nextLong()}",
            )
            val product4 = Listing(
                description = "a key",
                name = "Product ${Random.nextLong()}",
            )

            listingRepo.createListing(product).collect {
                when (it) {
                    is ApiResponse.Success -> listing = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            listingRepo.createListing(product2).collect {
                when (it) {
                    is ApiResponse.Success -> listing2 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            listingRepo.createListing(product3).collect {
                when (it) {
                    is ApiResponse.Success -> listing3 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            listingRepo.createListing(product4).collect {
                when (it) {
                    is ApiResponse.Success -> listing4 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }


        }
        runBlocking {
            listingRepo.getListingsBySearch("a key", mapOf()).collect {
                when (it) {
                    is ApiResponse.Success -> assert(
                        it.data.contains(listing3) && it.data.contains(
                            listing4
                        )
                    )
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }

    @Test
    fun getListingsBySearchShouldNotFailOnName() {
        lateinit var listing: Listing
        lateinit var listing2: Listing
        lateinit var listing3: Listing
        lateinit var listing4: Listing
        runBlocking {

            val product = Listing(
                description = Random.nextLong().toString(),
                name = "Product 1",
            )
            val product2 = Listing(
                description = Random.nextLong().toString(),
                name = "Product 2",
            )
            val product3 = Listing(
                description = Random.nextLong().toString(),
                name = "Product 3",
            )
            val product4 = Listing(
                description = Random.nextLong().toString(),
                name = "Product 4",
            )

            listingRepo.createListing(product).collect {
                when (it) {
                    is ApiResponse.Success -> listing = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            listingRepo.createListing(product2).collect {
                when (it) {
                    is ApiResponse.Success -> listing2 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            listingRepo.createListing(product3).collect {
                when (it) {
                    is ApiResponse.Success -> listing3 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            listingRepo.createListing(product4).collect {
                when (it) {
                    is ApiResponse.Success -> listing4 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }


        }
        runBlocking {
            listingRepo.getListingsBySearch("Product 3", mapOf()).collect {
                when (it) {
                    is ApiResponse.Success -> assert(
                        it.data.contains(listing3)
                    )
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }

    @Test
    fun getListingsBySearchshouldNotFailonPriceRange() {
        lateinit var listing: Listing
        lateinit var listing2: Listing
        lateinit var listing3: Listing
        lateinit var listing4: Listing
        runBlocking {

            val product = Listing(
                description = Random.nextLong().toString(),
                name = "Product 1",
                price = 1000F
            )
            val product2 = Listing(
                description = Random.nextLong().toString(),
                name = "Product 2",
                price = 1200F
            )
            val product3 = Listing(
                description = Random.nextLong().toString(),
                name = "Product 3",
                price = 1700F
            )
            val product4 = Listing(
                description = Random.nextLong().toString(),
                name = "Product 4",
                price = 2000F
            )

            listingRepo.createListing(product).collect {
                when (it) {
                    is ApiResponse.Success -> listing = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            listingRepo.createListing(product2).collect {
                when (it) {
                    is ApiResponse.Success -> listing2 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            listingRepo.createListing(product3).collect {
                when (it) {
                    is ApiResponse.Success -> listing3 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            listingRepo.createListing(product4).collect {
                when (it) {
                    is ApiResponse.Success -> listing4 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }


        }
        runBlocking {
            listingRepo.getListingsBySearch("1200-2000", mapOf()).collect {
                when (it) {
                    is ApiResponse.Success -> assert(
                        it.data.contains(listing4) && it.data.contains(listing2) && it.data.contains(listing3)
                                && !(it.data.contains(listing)))
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }

    @Test
    fun getListingsByMinShouldCountUp() {
        lateinit var listing: Listing
        lateinit var listing2: Listing
        lateinit var listing3: Listing
        lateinit var listing4: Listing
        runBlocking {

            val product = Listing(
                description = Random.nextLong().toString(),
                name = "Product 1",
                price = 1000F
            )
            val product2 = Listing(
                description = Random.nextLong().toString(),
                name = "Product 2",
                price = 1200F
            )
            val product3 = Listing(
                description = Random.nextLong().toString(),
                name = "Product 3",
                price = 1700F
            )
            val product4 = Listing(
                description = Random.nextLong().toString(),
                name = "Product 4",
                price = 2000F
            )

            listingRepo.createListing(product).collect {
                when (it) {
                    is ApiResponse.Success -> listing = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            listingRepo.createListing(product2).collect {
                when (it) {
                    is ApiResponse.Success -> listing2 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            listingRepo.createListing(product3).collect {
                when (it) {
                    is ApiResponse.Success -> listing3 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            listingRepo.createListing(product4).collect {
                when (it) {
                    is ApiResponse.Success -> listing4 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }


        }
        runBlocking {
            listingRepo.getListingsByMin("1700").collect {
                when (it) {
                    is ApiResponse.Success -> assert(
                        it.data.contains(listing4)  && it.data.contains(listing3)
                                && !(it.data.contains(listing)) && !(it.data.contains(listing2)) )
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }


    }
}


