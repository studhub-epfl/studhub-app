package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    /**
     * Adds a [message] to the repository
     * @param [message] the message to add to the repository
     * @return A [Flow] of [ApiResponse] with the last one containing the [Message] pushed to the repository on success
     */
    suspend fun createMessage(message: Message): Flow<ApiResponse<Message>>

    /**
     * Retrieves all messages from the repository sent in the given [conversation]
     * @param [conversation] the conversation which to retrieve messages from
     * @return A [Flow] of [ApiResponse] with the last one containing the retrieved list of [Message]s on success
     */
    suspend fun getConversationMessages(conversation: Conversation): Flow<ApiResponse<List<Message>>>

    /**
     * Flushes all (locally) pending messages sent by the given [user] to the repository
     * @param user the user who sent the messages to flush
     * @return A [Flow] of [ApiResponse] with the last one containing true iff all messages could be sent
     */
    suspend fun flushPendingMessages(user: User): Flow<ApiResponse<Boolean>>
}
