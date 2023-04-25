package com.studhub.app.data.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ConversationRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Singleton

@Singleton
class ConversationRepositoryImpl : ConversationRepository {

    private val db = Firebase.database.getReference("conversations")
    private val userDb = Firebase.database.getReference("users")

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

    override suspend fun getConversation(
        user: User,
        conversationId: String
    ): Flow<ApiResponse<Conversation>> =
        flow {
            emit(ApiResponse.Loading)

            val query = db.child(conversationId).get()

            query.await()

            if (query.isSuccessful) {
                val retrievedConversation: Conversation? =
                    query.result.getValue(Conversation::class.java)
                if (retrievedConversation == null) {
                    emit(ApiResponse.Failure("Conversation does not exist"))
                } else {
                    val user1Query = userDb.child(retrievedConversation.user1Id).get()
                    val user2Query = userDb.child(retrievedConversation.user2Id).get()

                    user1Query.await()
                    user2Query.await()

                    if (user1Query.isSuccessful && user2Query.isSuccessful) {
                        val conversation = retrievedConversation.copy(
                            user1 = user1Query.result.getValue(User::class.java),
                            user2 = user2Query.result.getValue(User::class.java)
                        )
                        emit(ApiResponse.Success(maybeSwitchUsers(conversation, user)))
                    } else {
                        val errorMessage1 = user1Query.exception?.message.orEmpty()
                        val errorMessage2 = user2Query.exception?.message.orEmpty()
                        Log.e("TAG", "User1 => $errorMessage1\nUser2 => $errorMessage2")
                        emit(ApiResponse.Failure("Error while retrieving users from conversation"))
                    }

                }
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }

    override suspend fun getUserConversations(user: User): Flow<ApiResponse<List<Conversation>>> =
        callbackFlow {
            trySend(ApiResponse.Loading)

            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val conversations = mutableListOf<Conversation>()

                    dataSnapshot.children.forEach { snapshot ->
                        val conversation = snapshot.getValue(Conversation::class.java)
                        if (conversation != null && (
                                    conversation.user1Id == user.id ||
                                            conversation.user2Id == user.id)
                        ) {
                            conversations.add(maybeSwitchUsers(conversation, user))
                        }
                    }

                    conversations.sortByDescending { it.updatedAt }

                    trySend(ApiResponse.Success(conversations))
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

    override suspend fun updateLastMessageWith(
        conversation: Conversation,
        message: Message
    ): Flow<ApiResponse<Conversation>> = flow {
        emit(ApiResponse.Loading)

        val conversationToPush =
            conversation.copy(updatedAt = Date(), lastMessageContent = message.content)

        val query = db.child(conversation.id).setValue(conversationToPush)

        query.await()

        if (query.isSuccessful) {
            emit(ApiResponse.Success(conversationToPush))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

    /**
     * Switch user1 and user2 in the [conversation] so that user1 is the given [currentUser]
     */
    private fun maybeSwitchUsers(conversation: Conversation, currentUser: User) =
        if (conversation.user1Id == currentUser.id)
            conversation
        else
            conversation.copy(
                user1Id = conversation.user2Id,
                user2Id = conversation.user1Id,
                user1 = conversation.user2,
                user2 = conversation.user1
            )
}

