package com.studhub.app.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.repository.MessageRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class MessageRepositoryImpl : MessageRepository {

    private val db = Firebase.database.getReference("messages")

    override suspend fun createMessage(message: Message): Flow<ApiResponse<Message>> {
        val messageId: String = db.push().key.orEmpty()
        val messageToPush: Message = message.copy(id = messageId)

        return flow {
            emit(ApiResponse.Loading)

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
            trySendBlocking(ApiResponse.Loading)

            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val messages = mutableListOf<Message>()

                    dataSnapshot.children.forEach { snapshot ->
                        val message = snapshot.getValue(Message::class.java)
                        if (message != null && message.conversationId == conversation.id) {
                            messages.add(message)
                        }
                    }

                    messages.sortBy { it.createdAt }

                    trySendBlocking(ApiResponse.Success(messages))
                }

                override fun onCancelled(error: DatabaseError) {
                    trySendBlocking(ApiResponse.Failure(error.message))
                }
            }

            db.addListenerForSingleValueEvent(listener)

            awaitClose {
                db.removeEventListener(listener)
                cancel()
            }
        }
}
