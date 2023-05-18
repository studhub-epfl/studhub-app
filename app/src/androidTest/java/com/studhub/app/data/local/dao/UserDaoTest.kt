package com.studhub.app.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.local.database.LocalAppDatabase
import com.studhub.app.domain.model.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
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
    fun setAndGetUser() {
        val userId = Random.nextLong().toString()
        val user1 = User(id = userId, userName = "Bobby")
        val user2 = User(id = Random.nextLong().toString(), userName = "Michelle")

        runBlocking {
            cache.saveUser(user1)
            cache.saveUser(user2)
        }

        var byId: User?

        runBlocking { byId = cache.getUser(userId) }

        assertEquals(user1.userName, byId!!.userName)
    }

    @Test
    @Throws(Exception::class)
    fun setAndGetAndDeleteAndGetUser() {
        val userId = Random.nextLong().toString()
        val user1 = User(id = userId, userName = "Bobby")
        val user2 = User(id = Random.nextLong().toString(), userName = "Michelle")

        // store 2 users
        runBlocking {
            cache.saveUser(user1)
            cache.saveUser(user2)
        }

        var byId: User?

        runBlocking { byId = cache.getUser(userId) }

        assertEquals(user1.userName, byId!!.userName)

        // delete 1 user
        runBlocking { cache.removeUser(userId) }

        // try fetching the deleted user
        runBlocking { byId = cache.getUser(userId) }

        assertNull(byId)
    }
}
