package com.studhub.app.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import com.studhub.app.domain.usecase.listing.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ListingRepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var listingRepo: ListingRepository

    @Inject
    lateinit var getListingsBySearch: GetListingsBySearch

    @Inject
    lateinit var createListing: CreateListing

    @Inject
    lateinit var getListing: GetListing

    @Inject
    lateinit var getListings: GetListings

    @Inject
    lateinit var removeListing: RemoveListing

    @Inject
    lateinit var updateListing: UpdateListing

    @Inject
    lateinit var updateListingToBidding: UpdateListingToBidding

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

            createListing.invoke(product).collect {
                when (it) {
                    is ApiResponse.Success -> listing = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }

        runBlocking {
            getListing.invoke(listing.id).collect {
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
            getListings.invoke().collect {
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
            updateListing.invoke(fakeListing.id, fakeListing).collect {
                when (it) {
                    is ApiResponse.Failure -> fail()
                    else -> assert(true)
                }
            }
        }
    }

    @Test
    fun updateListingToBiddingShouldNotFail() {
        val realListing = Listing(id = "-NVLrtm2gxN7_HzIiSZu")
        val deadline = Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000) // tomorrow
        val startingPrice = 150.0F
        runBlocking {
            updateListingToBidding.invoke(realListing, startingPrice, deadline).collect {
                when (it) {
                    is ApiResponse.Success -> assert(
                        it.data.biddingDeadline == deadline &&
                                it.data.price == startingPrice
                    )
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }

    @Test
    fun removeListingShouldNotFail() {
        // stub test
        val fakeListing = Listing(id = "000", name = "My listing")

        runBlocking {
            removeListing.invoke(fakeListing.id).collect {
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
                price = 2F
            )
            val product2 = Listing(
                description = Random.nextLong().toString(),
                name = "Product ${Random.nextLong()}",
                price = 3F
            )
            val product3 = Listing(
                description = "a key",
                name = "Product ${Random.nextLong()}",
                price = 4F
            )
            val product4 = Listing(
                description = "a key",
                name = "Product ${Random.nextLong()}",
                price = 5F
            )

            createListing.invoke(product).collect {
                when (it) {
                    is ApiResponse.Success -> listing = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            createListing.invoke(product2).collect {
                when (it) {
                    is ApiResponse.Success -> listing2 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            createListing.invoke(product3).collect {
                when (it) {
                    is ApiResponse.Success -> listing3 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            createListing.invoke(product4).collect {
                when (it) {
                    is ApiResponse.Success -> listing4 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
        runBlocking {
            getListingsBySearch.invoke("a key","0","10").collect {
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
                price = 2F
            )
            val product2 = Listing(
                description = Random.nextLong().toString(),
                name = "Product 2",
                price = 3F
            )
            val product3 = Listing(
                description = Random.nextLong().toString(),
                name = "Product 3",
                price = 4F
            )
            val product4 = Listing(
                description = Random.nextLong().toString(),
                name = "Product 4",
                price = 5F
            )

            createListing.invoke(product).collect {
                when (it) {
                    is ApiResponse.Success -> listing = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            createListing.invoke(product2).collect {
                when (it) {
                    is ApiResponse.Success -> listing2 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            createListing.invoke(product3).collect {
                when (it) {
                    is ApiResponse.Success -> listing3 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            createListing.invoke(product4).collect {
                when (it) {
                    is ApiResponse.Success -> listing4 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
        runBlocking {
            getListingsBySearch.invoke("Product 3", "0", "10").collect {
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


/*
    @Test
    fun getListingsBySearchShouldLeaveOutUnrangedProductsWithDifferentDescription() {
        lateinit var listing: Listing
        lateinit var listing2: Listing
        lateinit var listing3: Listing
        lateinit var listing4: Listing

        runBlocking {


            val product = Listing(
                description = "description1",
                name = "Product 1",
                price = 1000F
            )
            val product2 = Listing(
                description = "description1",
                name = "Product 2",
                price = 1002F
            )
            val product3 = Listing(
                description = "description2",
                name = "Product 3",
                price = 1700F
            )
            val product4 = Listing(
                description = "description2",
                name = "Product 4",
                price = 1702F
            )

            createListing.invoke(product).collect {
                when (it) {
                    is ApiResponse.Success -> listing = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            createListing.invoke(product2).collect {
                when (it) {
                    is ApiResponse.Success -> listing2 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            createListing.invoke(product3).collect {
                when (it) {
                    is ApiResponse.Success -> listing3 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            createListing.invoke(product4).collect {
                when (it) {
                    is ApiResponse.Success -> listing4 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
        runBlocking {
            getListingsBySearch.invoke("description1","1002", "1700").collect {
                when (it) {
                    is ApiResponse.Success -> assert(
                        it.data.contains(listing2)
                                && !(it.data.contains(listing)) && !(it.data.contains(listing4)) && !(it.data.contains(listing3))
                    )
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }
    */
/*
    @Test
    fun getListingsBySearchShouldFailOnNonNumericalInputs() {
        lateinit var listing: Listing
        lateinit var listing2: Listing
        runBlocking {

            val product = Listing(
                description = Random.nextLong().toString(),
                name = "Product 1",
                price = 1000F
            )
            val product2 = Listing(
                description = Random.nextLong().toString(),
                name = "Product 2",
                price = 1002F
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
        }
        runBlocking {
            getListingsBySearch.invoke("randomDescription","r", "1700").collect {
                when (it) {
                    is ApiResponse.Success -> {}
                    is ApiResponse.Failure -> assert(true)
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }
*/
    @Test
    fun getListingsBySearchShouldLeaveOutUnrangedProductsWithDescription() {
        lateinit var listing: Listing
        lateinit var listing2: Listing
        lateinit var listing3: Listing
        lateinit var listing4: Listing

        runBlocking {


            val product = Listing(
                description = "description1",
                name = "Product 1",
                price = 1000F
            )
            val product2 = Listing(
                description = "description1",
                name = "Product 2",
                price = 1002F
            )
            val product3 = Listing(
                description = "description1",
                name = "Product 3",
                price = 1700F
            )
            val product4 = Listing(
                description = "description1",
                name = "Product 4",
                price = 1702F
            )

            createListing.invoke(product).collect {
                when (it) {
                    is ApiResponse.Success -> listing = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            createListing.invoke(product2).collect {
                when (it) {
                    is ApiResponse.Success -> listing2 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            createListing.invoke(product3).collect {
                when (it) {
                    is ApiResponse.Success -> listing3 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
            createListing.invoke(product4).collect {
                when (it) {
                    is ApiResponse.Success -> listing4 = it.data
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
        runBlocking {
            getListingsBySearch.invoke("description1","1002", "1700").collect {
                when (it) {
                    is ApiResponse.Success -> assert(
                        it.data.contains(listing2) && it.data.contains(listing3)
                                && !(it.data.contains(listing)) && !(it.data.contains(listing4))
                    )
                    is ApiResponse.Failure -> fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }

    @Test
    fun getListingsByRangeShouldFailOnVoidSearch() {
        lateinit var listing: Listing
        lateinit var listing2: Listing
        runBlocking {

            val product = Listing(
                description = "",
                name = "Product 1",
                price = 1000F
            )
            val product2 = Listing(
                description = "",
                name = "Product 2",
                price = 1002F
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
        }
        runBlocking {

            getListingsBySearch.invoke("","r", "1700").collect {
                when (it) {
                    is ApiResponse.Success -> {}
                    is ApiResponse.Failure -> assert(true)
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }
    @Test
    fun getListingsByRangeShouldFailOnSmallSearchValues() {
        lateinit var listing: Listing
        lateinit var listing2: Listing
        runBlocking {

            val product = Listing(
                description = "",
                name = "Product 1",
                price = 1000F
            )
            val product2 = Listing(
                description = "",
                name = "Product 2",
                price = 1002F
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
        }
        runBlocking {

            getListingsBySearch.invoke("e","0", "1700").collect {
                when (it) {
                    is ApiResponse.Success -> {}
                    is ApiResponse.Failure -> assert(true)
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }


}


