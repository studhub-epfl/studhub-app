package com.studhub.app.data.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.network.NetworkStatus
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ConversationRepository
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
class ConversationRepositoryImpl @Inject constructor(
    private val remoteDb: FirebaseDatabase,
    private val localDb: LocalDataSource,
    private val networkStatus: NetworkStatus
) : ConversationRepository {

    private val db = remoteDb.getReference("conversations")
    private val userDb = remoteDb.getReference("users")

    override suspend fun createConversation(conversation: Conversation): Flow<ApiResponse<Conversation>> =
        flow {
            emit(ApiResponse.Loading)

            if (!networkStatus.isConnected) {
                emit(ApiResponse.NO_INTERNET_CONNECTION)
                return@flow
            }

            // first check that the user is not trying to contact themself
            if (conversation.user1Id == conversation.user2Id) {
                emit(ApiResponse.Failure("User cannot contact itself"))
                return@flow
            }

            // then check whether there already exists a conversation between the two users
            val getQuery = db.get()
            getQuery.await()
            if (!getQuery.isSuccessful) {
                emit(
                    ApiResponse.Failure(
                        getQuery.exception?.message.orEmpty()
                            .ifEmpty { "DB Error while looking for existing conversation" })
                )
                return@flow
            }

            if (getQuery.result.exists()) {
                for (convoSnapshot in getQuery.result.children) {
                    val convo: Conversation? = convoSnapshot.getValue(Conversation::class.java)
                    if (convo != null) {
                        if (
                            (convo.user1Id == conversation.user1Id && convo.user2Id == conversation.user2Id) ||
                            (convo.user1Id == conversation.user2Id && convo.user2Id == conversation.user1Id)
                        ) {
                            emit(ApiResponse.Success(convo))
                            return@flow
                        }
                    }
                }
            }

            // get both users' informations in order to store the names
            val user1Query = userDb.child(conversation.user1Id).get()
            val user2Query = userDb.child(conversation.user2Id).get()

            user1Query.await()
            user2Query.await()

            if (!user1Query.isSuccessful || !user2Query.isSuccessful) {
                val errorMessage1 = user1Query.exception?.message.orEmpty()
                val errorMessage2 = user2Query.exception?.message.orEmpty()
                Log.e("TAG", "User1 => $errorMessage1\nUser2 => $errorMessage2")
                emit(ApiResponse.Failure("Error while retrieving users from conversation"))
                return@flow
            }

            val user1 = user1Query.result.getValue<User>()
            val user2 = user2Query.result.getValue<User>()

            // if no such conversation exists, create it
            val conversationId: String = db.push().key.orEmpty()
            val conversationToPush: Conversation = conversation.copy(
                id = conversationId,
                user1Name = user1?.userName.orEmpty(),
                user2Name = user2?.userName.orEmpty()
            )

            val query = db.child(conversationId).setValue(conversationToPush)

            query.await()

            if (query.isSuccessful) {
                emit(ApiResponse.Success(conversationToPush))
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }

    override suspend fun getConversation(
        user: User,
        conversationId: String
    ): Flow<ApiResponse<Conversation>> =
        flow {
            emit(ApiResponse.Loading)

            if (!networkStatus.isConnected) {
                emit(
                    ApiResponse.Success(
                        // reorder just in case 2 users used the same phone and sent each other some messages
                        maybeSwitchUsers(
                            getCachedConversation(conversationId),
                            user
                        )
                    )
                )
                return@flow
            }

            val query = db.child(conversationId).get()

            query.await()

            if (query.isSuccessful) {
                val retrievedConversation: Conversation? =
                    query.result.getValue(Conversation::class.java)
                if (retrievedConversation == null) {
                    emit(ApiResponse.Failure("Conversation does not exist"))
                } else {
                    emit(ApiResponse.Success(maybeSwitchUsers(retrievedConversation, user)))
                }
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }

    override suspend fun getUserConversations(user: User): Flow<ApiResponse<List<Conversation>>> =
        callbackFlow {
            trySend(ApiResponse.Loading)

            if (!networkStatus.isConnected) {
                // reorder just in case 2 users used the same phone and sent each other some messages
                val conversations =
                    getCachedConversations(user.id).map { maybeSwitchUsers(it, user) }
                trySend(ApiResponse.Success(conversations))
            }

            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val conversations = mutableListOf<Conversation>()

                    dataSnapshot.children.forEach { snapshot ->
                        val conversation = snapshot.getValue(Conversation::class.java)
                        if (conversation != null && (
                                    conversation.user1Id == user.id ||
                                            conversation.user2Id == user.id)
                        ) {
                            // ensure logged-in user is User1
                            val conv: Conversation = maybeSwitchUsers(conversation, user)
                            cacheConversation(conv)
                            conversations.add(conv)
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
                user1Name = conversation.user2Name,
                user2Name = conversation.user1Name,
                user1 = conversation.user2,
                user2 = conversation.user1
            )

    private fun cacheConversation(conversation: Conversation) {
        runBlocking {
            localDb.saveConversation(conversation)
        }
    }

    private suspend fun getCachedConversations(userId: String): List<Conversation> {
        return localDb.getConversations(userId)
    }

    private suspend fun getCachedConversation(conversationId: String): Conversation {
        return localDb.getConversation(conversationId)
    }
}

