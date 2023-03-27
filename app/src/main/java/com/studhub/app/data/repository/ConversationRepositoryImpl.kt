package com.studhub.app.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ConversationRepository
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class ConversationRepositoryImpl : ConversationRepository {

    private val db = Firebase.database.getReference("conversations")

    override suspend fun createConversation(conversation: Conversation): Flow<ApiResponse<Conversation>> {
        val conversationId: String = db.push().key.orEmpty()
        val conversationToPush: Conversation = conversation.copy(id = conversationId)

        return flow {
            emit(ApiResponse.Loading)

            val query = db.child(conversationId).setValue(conversationToPush)

            query.await()

            if (query.isSuccessful) {
                emit(ApiResponse.Success(conversationToPush))
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }
    }

    override suspend fun getConversation(conversationId: String): Flow<ApiResponse<Conversation>> =
        flow {
            emit(ApiResponse.Loading)

            val query = db.child(conversationId).get()

            query.await()

            if (query.isSuccessful) {
                val retrievedConversation: Conversation? =
                    query.result.getValue(Conversation::class.java)
                if (retrievedConversation == null) {
                    emit(ApiResponse.Failure("Listing does not exist"))
                } else {
                    emit(ApiResponse.Success(retrievedConversation))
                }
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }

    override suspend fun getUserConversations(user: User): Flow<ApiResponse<List<Conversation>>> =
        callbackFlow {
            trySendBlocking(ApiResponse.Loading)

            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val conversations = mutableListOf<Conversation>()

                    dataSnapshot.children.forEach { snapshot ->
                        val conversation = snapshot.getValue(Conversation::class.java)
                        if (conversation != null && (
                                    conversation.user1Id == user.id ||
                                            conversation.user2Id == user.id)
                        ) {
                            conversations.add(conversation)
                        }
                    }

                    conversations.sortBy { it.updatedAt }

                    trySendBlocking(ApiResponse.Success(conversations))
                }

                override fun onCancelled(error: DatabaseError) {
                    trySendBlocking(ApiResponse.Failure(error.message))
                }
            }

            db.addListenerForSingleValueEvent(listener)
        }
}
