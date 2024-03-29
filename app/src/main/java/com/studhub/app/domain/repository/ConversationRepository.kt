package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
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
     * Retrieves the [Conversation] with the given [conversationId] from the repository
     *
     * `User1` of the [Conversation] should be [user]
     *
     * @param [conversationId] the id of the [Conversation] to retrieve
     * @return A [Flow] of [ApiResponse] with the last one containing the [Conversation] retrieved from the repository on success
     */
    suspend fun getConversation(user: User, conversationId: String): Flow<ApiResponse<Conversation>>

    /**
     * Retrieves all conversations from the repository sent by the given [user]
     *
     * `User1` of each [Conversation] should be [user]
     * @param [user] the user which to retrieve conversations from
     * @return A [Flow] of [ApiResponse] with the last one containing the retrieved list of [Conversation]s on success
     */
    suspend fun getUserConversations(user: User): Flow<ApiResponse<List<Conversation>>>

}
