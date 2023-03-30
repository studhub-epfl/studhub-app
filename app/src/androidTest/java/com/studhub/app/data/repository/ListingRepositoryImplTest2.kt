package com.studhub.app.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.CategoryRepository
import com.studhub.app.domain.repository.ListingRepository
import com.studhub.app.domain.usecase.listing.CreateListing
import com.studhub.app.domain.usecase.listing.GetListing
import com.studhub.app.domain.usecase.listing.GetListings
import com.studhub.app.domain.usecase.listing.GetListingsBySearch
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.fail

import javax.inject.Singleton



    @Module
    @InstallIn(SingletonComponent::class)
    object TestFirebaseModule {

        @Provides
        fun provideFirebaseDatabase(): DatabaseReference {
            return Firebase.database.reference.child("test-listings")
        }

    }

    @HiltAndroidTest
    class ListingRepositoryImplTest2 {

        @get:Rule
        var hiltRule = HiltAndroidRule(this)

        @Inject
        lateinit var repository: ListingRepository

        @Inject
        lateinit var firebaseDatabase: DatabaseReference

        @Before
        fun setUp() {
            hiltRule.inject()
        }

        @Test
        fun testCreateListing() = runBlockingTest {
            val listing = Listing("Test Listing", "This is a test listing.")
            val flow = repository.createListing(listing)

            flow.collect {
                when (it) {
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {
                        assertEquals(listing.name, it.data.name)
                        assertEquals(listing.description, it.data.description)
                    }
                    is ApiResponse.Failure -> fail(it.message)
                }
            }
        }

        @Test
        fun testGetListings() = runBlockingTest {
            val listing1 = Listing("Listing 1", "This is listing 1.")
            val listing2 = Listing("Listing 2", "This is listing 2.")

            firebaseDatabase.child("listing1").setValue(listing1).await()
            firebaseDatabase.child("listing2").setValue(listing2).await()

            val flow = repository.getListings()

            flow.collect {
                when (it) {
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {
                        assertEquals(2, it.data.size)
                        assertTrue(it.data.contains(listing1))
                        assertTrue(it.data.contains(listing2))
                    }
                    is ApiResponse.Failure -> fail(it.message)
                }
            }
        }

        @Test
        fun testGetListing() = runBlockingTest {
            val listing = Listing("Test Listing", "This is a test listing.")
            firebaseDatabase.child("test-listing").setValue(listing).await()

            val flow = repository.getListing("test-listing")

            flow.collect {
                when (it) {
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Success -> assertEquals(listing, it.data)
                    is ApiResponse.Failure -> fail(it.message)
                }
            }
        }

        @Test
        fun testGetListingsBySearch() = runBlockingTest {
            val listing1 = Listing("Listing 1", "This is listing 1.")
            val listing2 = Listing("Listing 2", "This is listing 2.")
            val listing3 = Listing("Test Listing", "This is a test listing.")

            firebaseDatabase.child("listing1").setValue(listing1).await()
            firebaseDatabase.child("listing2").setValue(listing2).await()
            firebaseDatabase.child("test-listing").setValue(listing3).await()

            val flow = repository.getListingsBySearch("test")

            flow.collect {
                when (it) {
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {
                        assertEquals(1, it.data.size)
                        assertTrue(it.data.contains(listing3))
                    }
                    is ApiResponse.Failure -> fail(it.message)
                }
            }
        }
        @Test
        fun testUpdateListing() = runBlockingTest {
            val listing = Listing("Test Listing", "This is a test listing.")
            firebaseDatabase.child("test-listing").setValue(listing).await()

            val updatedListing = listing.copy(name = "Updated Test Listing ", description = "This is an updated test listing.")
            val flow = repository.updateListing("test-listing", updatedListing)
            flow.collect {
                when (it) {
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Success -> assertEquals(updatedListing, it.data)
                    is ApiResponse.Failure -> fail(it.message)
                }
            }

            val databaseListing = firebaseDatabase.child("test-listing").get().await().getValue(Listing::class.java)
            assertEquals(updatedListing, databaseListing)
        }
        @Test
        fun testDeleteListing() = runBlockingTest {
            val listing = Listing("Test Listing", "This is a test listing.")
            firebaseDatabase.child("test-listing").setValue(listing).await()

            val flow = repository.removeListing("test-listing")

            flow.collect {
                when (it) {
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {}
                    is ApiResponse.Failure -> fail(it.message)
                }
            }

            val databaseListing = firebaseDatabase.child("test-listing").get().await().getValue(Listing::class.java)
            assertNull(databaseListing)
        }

    }



            /*
             private val mockListing = Listing(
                 id = "1",
                 name = "Mock Listing",
                 description = "A mock listing for testing",
                 seller = User(
                     id = "wiufhb",
                     userName = "Stud Hub",
                     email = "stud.hub@studhub.ch"
                 ),
                 price = 0F,
                 categories = emptyList()
             )
         */



