package com.studhub.app.data.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.network.NetworkStatus
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.MessageRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepositoryImpl @Inject constructor(
    private val remoteDb: FirebaseDatabase,
    private val localDb: LocalDataSource,
    private val networkStatus: NetworkStatus
) : MessageRepository {

    private val rootDb = remoteDb.reference
    private val db = rootDb.child("messages")

    override suspend fun createMessage(message: Message): Flow<ApiResponse<Message>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            bufferMessage(message)
            emit(ApiResponse.Success(message))
            return@flow
        }

        val (pushedMessage, updates) = getCreateMessageQuery(message)

        val query = rootDb.updateChildren(updates)
        query.await()

        if (query.isSuccessful) {
            emit(ApiResponse.Success(pushedMessage))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

    override suspend fun getConversationMessages(conversation: Conversation): Flow<ApiResponse<List<Message>>> =
        callbackFlow {
            trySend(ApiResponse.Loading)

            if (!networkStatus.isConnected) {
                trySend(
                    ApiResponse.Success(
                        getCachedConversationMessages(conversation.id) +
                                // also return unsent messages at the end
                                getBufferedMessages(conversation.id)
                    )
                )
            }

            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val messages = mutableListOf<Message>()

                    dataSnapshot.children.forEach { snapshot ->
                        val message = snapshot.getValue(Message::class.java)
                        if (message != null && message.conversationId == conversation.id) {
                            cacheMessage(message)
                            messages.add(message)
                        }
                    }

                    messages.sortBy { it.createdAt }

                    trySend(ApiResponse.Success(messages))
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(ApiResponse.Failure(error.message))
                }
            }

            db.addValueEventListener(listener)

            awaitClose {
                db.removeEventListener(listener)
                cancel()
            }
        }

    override suspend fun flushPendingMessages(user: User): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        val messages = getBufferedMessagesOfUser(user.id)

        // if there is no message to flush, just return
        if (messages.isEmpty()) {
            emit(ApiResponse.Success(true))
            return@flow
        }

        val updates = messages
            // map the list of messages to a list of pairs of Message and Updates (<path, value>)
            .map { getCreateMessageQuery(it) }
            // concatenate the updates to have a single big update (map of <path, value>)
            .fold(mapOf<String, Any>()) { acc, value -> acc.plus(value.second) }

        val query = rootDb.updateChildren(updates)
        query.await()

        if (query.isSuccessful) {
            // on success, remove all flushed messages from the cache
            unbufferAllMessages(user.id)
            emit(ApiResponse.Success(true))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            Log.w("MESSAGE_REPO", errorMessage)
            emit(ApiResponse.Failure("Database error"))
        }
    }

    /**
     * Returns a pair with the first element being the pushed message and the second being the queries to send to the database system
     *
     * @param message the message to upload
     * @return a pair with the first element being the pushed message and the second being the queries to send to the database system
     */
    private fun getCreateMessageQuery(message: Message): Pair<Message, Map<String, Any>> {
        val messageId: String = db.push().key.orEmpty()
        val messageToPush: Message = message.copy(id = messageId, createdAt = Date())

        val updates = mapOf(
            "/messages/$messageId" to messageToPush,
            "/conversations/${message.conversationId}/updatedAt" to Date(),
            "/conversations/${message.conversationId}/lastMessageContent" to messageToPush.content,
        )

        return Pair(messageToPush, updates)
    }

    private fun cacheMessage(message: Message) {
        runBlocking {
            localDb.saveMessage(message)
        }
    }

    private suspend fun getCachedConversationMessages(conversationId: String): List<Message> =
        localDb.getMessages(conversationId)

    private suspend fun bufferMessage(message: Message) {
        localDb.saveUnsentMessage(message)
    }

    private suspend fun unbufferAllMessages(userId: String) {
        localDb.removeUnsentMessagesOfUser(userId)
    }

    private suspend fun getBufferedMessagesOfUser(userId: String): List<Message> =
        localDb.getUnsentMessagesOfUser(userId)

    private suspend fun getBufferedMessages(conversationId: String): List<Message> =
        localDb.getUnsentMessagesOfConversation(conversationId)
}
