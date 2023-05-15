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
class UnsentMessageDaoTest {
    private lateinit var localDb: LocalAppDatabase
    private lateinit var cache: LocalDataSource

    private val conversationId = Random.nextLong().toString()
    private val message1 = Message(
        conversationId = conversationId,
        senderId = "Bobby",
        createdAt = Date(1000),
        content = Random.nextLong().toString()
    )
    private val message2 = Message(
        conversationId = conversationId,
        senderId = "Bobby",
        createdAt = Date(99999),
        content = Random.nextLong().toString()
    )
    private val message3 = Message(
        senderId = "Bobby",
        conversationId = Random.nextLong().toString(),
        content = Random.nextLong().toString()
    )
    private val message4 = Message(
        senderId = "Maddy",
        conversationId = Random.nextLong().toString(),
        content = Random.nextLong().toString()
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
    fun setAndGetMessagesByConversation() {
        // store the latest message between message1 and message2
        // this is to test that they are retrieved from the oldest to the latest and not in insertion order
        runBlocking { cache.saveUnsentMessage(message2) }

        runBlocking {
            cache.saveUnsentMessage(message1)
            cache.saveUnsentMessage(message3)
        }

        var messages: List<Message>

        runBlocking { messages = cache.getUnsentMessagesOfConversation(conversationId) }

        assertEquals("The given conversation should only have 2 messages", 2, messages.size)
        assertEquals(
            "First message should be the earlier one (message1)",
            message1.content,
            messages.first().content
        )
        assertEquals(
            "Second and last message should be the latest message (message2)",
            message2.content,
            messages.last().content
        )
    }

    @Test
    @Throws(Exception::class)
    fun setAndGetMessagesByUser() {
        // store the latest message between message1 and message3
        // this is to test that they are retrieved from the oldest to the latest and not in insertion order
        runBlocking { cache.saveUnsentMessage(message3) }

        runBlocking {
            cache.saveUnsentMessage(message1)
            cache.saveUnsentMessage(message2)
        }

        var messages: List<Message>

        runBlocking { messages = cache.getUnsentMessagesOfUser("Bobby") }

        assertEquals("The given user should only have 3 messages", 3, messages.size)
        assertEquals(
            "First message should be the earlier one (message1)",
            message1.content,
            messages.first().content
        )
        assertEquals(
            "Second and last message should be the latest message (message2)",
            message3.content,
            messages.last().content
        )
    }

    @Test
    fun removeAllUnsentMessagesOfUserWorksCorrectly() {
        // store messages
        runBlocking {
            cache.saveUnsentMessage(message1) // sent by Bobby
            cache.saveUnsentMessage(message2) // sent by Bobby
            cache.saveUnsentMessage(message3) // sent by Bobby
            cache.saveUnsentMessage(message4) // sent by Maddy
        }

        // remove all messages sent (in offline mode) by Bobby
        runBlocking { cache.removeUnsentMessagesOfUser(message1.senderId) }

        var messages: List<Message>

        runBlocking { messages = cache.getUnsentMessagesOfUser("Bobby") }

        assertEquals("Bobby should have no unsent messages", 0, messages.size)

        runBlocking { messages = cache.getUnsentMessagesOfUser("Maddy") }

        assertTrue("Maddy's unsent messages were not removed accidentally", messages.isNotEmpty())
    }
}
