package com.studhub.app.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.local.database.LocalAppDatabase
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.Message
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
class UserFavListingsDaoTest {
    private lateinit var localDb: LocalAppDatabase
    private lateinit var cache: LocalDataSource

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
    fun addAndRemoveFavoriteListing() {
        val userId = Random.nextLong().toString()
        val listingId = Random.nextLong().toString()
        val listing = Listing(
            id = listingId,
            sellerId = userId,
            createdAt = Date(1000)
        )

        // store listing and add it to user's favorite
        runBlocking {
            cache.saveFavoriteListing(userId, listing)
        }

        var favorites: List<Listing>

        runBlocking { favorites = cache.getFavListings(userId) }

        assertEquals("User has 1 favorite", 1, favorites.size)

        // remove user's fav listing
        runBlocking { cache.removeFavoriteListing(userId, listing.id) }

        runBlocking { favorites = cache.getFavListings(userId) }

        assertEquals("User has no favorites", 0, favorites.size)
    }

    @Test
    @Throws(Exception::class)
    fun onlyGivenUserFavListingsAreRetrieved() {
        val userId = Random.nextLong().toString()
        val michelle = User(id = "Michelle")
        val listing1Id = Random.nextLong().toString()
        val listing2Id = Random.nextLong().toString()
        val listing1 = Listing(
            id = listing1Id,
            sellerId = userId,
            createdAt = Date(1000)
        )
        val listing2 = Listing(
            id = listing2Id,
            sellerId = userId,
            createdAt = Date(1000)
        )
        val listing3 = Listing(
            id = Random.nextLong().toString(),
            sellerId = "Michelle",
            createdAt = Date(1000)
        )

        // store listings and
        // - add listings 1 and 2 to user's favorite
        // - add listing 3 to Michelle's favorite
        runBlocking {
            cache.saveFavoriteListing(userId, listing1)
            cache.saveFavoriteListing(userId, listing2)
            cache.saveFavoriteListing(michelle.id, listing3)
        }

        var favListings: List<Listing>

        runBlocking { favListings = cache.getFavListings(userId) }

        assertEquals("The given user should only have 2 fav listings", 2, favListings.size)

        val listing1Singleton = favListings.filter { it.id == listing1Id }
        val listing2Singleton = favListings.filter { it.id == listing2Id }

        assertEquals(
            "There is 1 fav listing which is listing1",
            1,
            listing1Singleton.size
        )

        assertEquals(
            "There is 1 fav listing which is listing2",
            1,
            listing2Singleton.size
        )

        assertEquals(
            "Listing1 is correctly retrieved",
            listing1Id,
            listing1Singleton.first().id
        )

        assertEquals(
            "Listing2 is correctly retrieved",
            listing2Id,
            listing2Singleton.first().id
        )
    }
}
