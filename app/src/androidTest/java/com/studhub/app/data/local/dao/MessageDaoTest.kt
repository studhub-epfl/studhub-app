package com.studhub.app.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.local.database.LocalAppDatabase
import com.studhub.app.domain.model.Message
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
class MessageDaoTest {
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
    fun setAndGetMessages() {
        val conversationId = Random.nextLong().toString()
        val message1Id = Random.nextLong().toString()
        val message2Id = Random.nextLong().toString()
        val message1 = Message(
            id = message1Id,
            conversationId = conversationId,
            senderId = "Bobby",
            createdAt = Date(1000)
        )
        val message2 =
            Message(
                id = message2Id,
                conversationId = conversationId,
                senderId = "Michelle",
                createdAt = Date(99999)
            )
        val message3 = Message(
            id = Random.nextLong().toString(),
            conversationId = Random.nextLong().toString()
        )


        // store the latest message between message1 and message2
        // this is to test that they are retrieved from the oldest to the latest and not in insertion order
        runBlocking { cache.saveMessage(message2) }

        runBlocking {
            cache.saveMessage(message1)
            cache.saveMessage(message3)
        }

        var messages: List<Message>

        runBlocking { messages = cache.getMessages(conversationId) }

        assertEquals("The given conversation should only have 2 messages", 2, messages.size)
        assertEquals(
            "First message should be the earlier one (message1)",
            message1.id,
            messages.first().id
        )
        assertEquals(
            "Second and last message should be the latest message (message2)",
            message2.id,
            messages.last().id
        )
    }
}
