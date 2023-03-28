package com.studhub.app.domain.usecase.conversation

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all messages of a given conversation from the given [messageRepository]
 *
 * @param [messageRepository] the repository which the use case will retrieve the messages from
 */
class GetConversationMessages @Inject constructor(private val messageRepository: MessageRepository) {

    /**
     * Retrieves all conversations of the connected user.
     */
    suspend operator fun invoke(conversation: Conversation): Flow<ApiResponse<List<Message>>> {
        return messageRepository.getConversationMessages(conversation)
    }
}
