package com.studhub.app.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.local.database.LocalAppDatabase
import com.studhub.app.domain.model.Conversation
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class ConversationDaoTest {
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
    fun setAndGetMessage() {
        val conversationId = Random.nextLong().toString()
        val conversation1 = Conversation(id = conversationId, user1Name = "Bobby")
        val conversation2 = Conversation(id = Random.nextLong().toString(), user1Name = "Michelle")

        runBlocking {
            cache.saveConversation(conversation1)
            cache.saveConversation(conversation2)
        }

        var byId: Conversation?

        runBlocking { byId = cache.getConversation(conversationId) }

        assertEquals(conversation1.user1Name, byId!!.user1Name)
    }

    @Test
    @Throws(Exception::class)
    fun getConversationsReturnsOnlyGivenUserConversations() {
        val userId = Random.nextLong().toString()
        val conversationId1 = Random.nextLong().toString()
        val conversationId2 = Random.nextLong().toString()
        val conversation1 = Conversation(
            id = conversationId1,
            user1Id = userId,
            user1Name = "Bobby",
            user2Name = "Michelle"
        )
        val conversation2 = Conversation(
            id = conversationId2,
            user2Id = userId,
            user1Name = "Jean-Marc",
            user2Name = "Bobby"
        )
        val conversation3 = Conversation(
            id = Random.nextLong().toString(),
            user1Id = "xxxx",
            user1Name = "Jean-Marc",
            user2Name = "Robert"
        )
        val conversation4 = Conversation(
            id = Random.nextLong().toString(),
            user2Id = "yyyy",
            user1Name = "Jean-Marc",
            user2Name = "Michelle"
        )

        runBlocking {
            cache.saveConversation(conversation1)
            cache.saveConversation(conversation2)
            cache.saveConversation(conversation3)
            cache.saveConversation(conversation4)
        }

        var convos: List<Conversation>

        runBlocking { convos = cache.getConversations(userId) }

        assertEquals("Only 2 conversations are Bobby's", 2, convos.size)

        val conversation1Singleton = convos.filter { it.user1Id == userId }
        val conversation2Singleton = convos.filter { it.user2Id == userId }

        assertEquals(
            "There is 1 conversation for which user is user1",
            1,
            conversation1Singleton.size
        )

        assertEquals(
            "Conversation 1 is correctly retrieved",
            conversationId1,
            conversation1Singleton.first().id
        )

        assertEquals(
            "There is 1 conversation for which user is user2",
            1,
            conversation2Singleton.size
        )

        assertEquals(
            "Conversation 2 is correctly retrieved",
            conversationId2,
            conversation2Singleton.first().id
        )

    }
}
