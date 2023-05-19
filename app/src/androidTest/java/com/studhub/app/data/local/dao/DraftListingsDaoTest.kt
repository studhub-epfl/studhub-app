package com.studhub.app.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.local.database.LocalAppDatabase
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class DraftListingsDaoTest {
    private lateinit var localDb: LocalAppDatabase
    private lateinit var cache: LocalDataSource

    private val listingId = Random.nextLong().toString()
    private val listing1 = Listing(
        id = listingId,
        sellerId = "Bobby",
        name = Random.nextLong().toString()
    )
    private val listing2 = Listing(
        id = Random.nextLong().toString(),
        sellerId = "Bobby",
        name = Random.nextLong().toString()
    )
    private val listing3 = Listing(
        id = Random.nextLong().toString(),
        sellerId = "Bobby",
        name = Random.nextLong().toString()
    )
    private val listing4 = Listing(
        id = Random.nextLong().toString(),
        sellerId = "Maddy",
        name = Random.nextLong().toString()
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        localDb = Room.inMemoryDatabaseBuilder(
            context, LocalAppDatabase::class.java
        ).build()
        cache = LocalDataSource(localDb)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        localDb.close()
    }


    @Test
    @Throws(Exception::class)
    fun setAndGetDraftListingsOfUser() {
        // one by one to make sure order of insertion is 1, 2, 3, 4
        runBlocking { cache.saveDraftListing(listing1) }
        runBlocking { cache.saveDraftListing(listing2) }
        runBlocking { cache.saveDraftListing(listing3) }
        runBlocking { cache.saveDraftListing(listing4) }

        var listings: List<Listing>

        runBlocking { listings = cache.getDraftListings(User(id = "Bobby")) }

        assertEquals("The given user should only have 3 draft listings", 3, listings.size)
        assertEquals(
            "First listing should be the latest one (listing3)",
            listing3.name,
            listings.first().name
        )
        assertEquals(
            "Last listing should be the earliest listing (listing1)",
            listing1.name,
            listings.last().name
        )
    }

    @Test
    fun getDraftListing() {
        var savedListing3: Listing

        runBlocking { cache.saveDraftListing(listing1) }
        runBlocking { savedListing3 = cache.saveDraftListing(listing3) }

        var retrievedListing: Listing

        runBlocking { retrievedListing = cache.getDraftListing(savedListing3.id) }

        assertEquals("Retrieved listing is the expected one", listing3.name, retrievedListing.name)
    }

    @Test
    fun removeDraftListings() {
        var savedListing1: Listing
        var savedListing3: Listing

        // ensured to work by the previous test => no need to test that the values are correctly set
        runBlocking { savedListing1 = cache.saveDraftListing(listing1) }
        runBlocking { cache.saveDraftListing(listing2) }
        runBlocking { savedListing3 = cache.saveDraftListing(listing3) }
        runBlocking { cache.saveDraftListing(listing4) }

        // arbitrarily remove drafts 1 and 3
        runBlocking {
            cache.removeDraftListing(savedListing1.id)
            cache.removeDraftListing(savedListing3.id)
        }

        var bobbyListings: List<Listing>
        var maddyListings: List<Listing>

        runBlocking { bobbyListings = cache.getDraftListings(User(id = "Bobby")) }
        runBlocking { maddyListings = cache.getDraftListings(User(id = "Maddy")) }

        assertEquals("Bobby only has 1 draft left", 1, bobbyListings.size)
        assertEquals("Maddy still has her only draft untouched", 1, maddyListings.size)

        assertEquals(
            "Bobby's remaining draft is the correct one",
            listing2.name,
            bobbyListings.first().name
        )
        assertEquals("Maddy's draft is the correct one", listing4.name, maddyListings.first().name)
    }
}
