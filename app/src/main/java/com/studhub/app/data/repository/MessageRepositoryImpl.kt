package com.studhub.app.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.network.NetworkStatus
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
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

    private val db = remoteDb.getReference("messages")

    override suspend fun createMessage(message: Message): Flow<ApiResponse<Message>> {
        val messageId: String = db.push().key.orEmpty()
        val messageToPush: Message = message.copy(id = messageId, createdAt = Date())

        return flow {
            emit(ApiResponse.Loading)

            if (!networkStatus.isConnected) {
                emit(ApiResponse.NO_INTERNET_CONNECTION)
                return@flow
            }

            val query = db.child(messageId).setValue(messageToPush)

            query.await()

            if (query.isSuccessful) {
                emit(ApiResponse.Success(messageToPush))
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }
    }

    override suspend fun getConversationMessages(conversation: Conversation): Flow<ApiResponse<List<Message>>> =
        callbackFlow {
            trySend(ApiResponse.Loading)

            if (!networkStatus.isConnected) {
                trySend(ApiResponse.Success(getCachedConversationMessages(conversation.id)))
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

    private fun cacheMessage(message: Message) {
        runBlocking {
            localDb.saveMessage(message)
        }
    }

    private suspend fun getCachedConversationMessages(conversationId: String): List<Message> {
        return localDb.getMessages(conversationId)
    }
}
