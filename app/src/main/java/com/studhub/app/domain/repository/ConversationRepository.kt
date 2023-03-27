package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {

    /**
     * Adds a [conversation] to the repository
     * @param [conversation] the conversation to add to the repository
     * @return A [Flow] of [ApiResponse] with the last one containing the [Conversation] pushed to the repository on success
     */
    suspend fun createConversation(conversation: Conversation): Flow<ApiResponse<Conversation>>

    /**
     * Retrieves a conversation from the repository with the given [conversationId]
     * @param [conversationId] the id of the conversation to retrieve
     * @return A [Flow] of [ApiResponse] with the last one containing the retrieved [Conversation] on success
     */
    suspend fun getConversation(conversationId: String): Flow<ApiResponse<Conversation>>

    /**
     * Retrieves all conversations from the repository sent by the given [user]
     * @param [user] the user which to retrieve conversations from
     * @return A [Flow] of [ApiResponse] with the last one containing the retrieved list of [Conversation]s on success
     */
    suspend fun getUserConversations(user: User): Flow<ApiResponse<List<Conversation>>>

}
