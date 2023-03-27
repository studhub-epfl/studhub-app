package com.studhub.app.domain.usecase.conversation

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for creating a conversation in the given [conversationRepository]
 *
 * @param [conversationRepository] the repository which the use case will create the conversation in
 */
class CreateConversation @Inject constructor(private val conversationRepository: ConversationRepository) {

    /**
     * Creates a conversation.
     */
    suspend operator fun invoke(conversation: Conversation): Flow<ApiResponse<Conversation>> {
        return conversationRepository.createConversation(conversation)
    }
}
