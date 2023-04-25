package com.studhub.app.domain.usecase.conversation

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting a conversation from the given [conversationRepository]
 *
 * @param [conversationRepository] the repository which the use case will retrieve the conversation from
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class GetConversation @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val authRepository: AuthRepository
) {

    /**
     * Retrieves the conversation with the given ID.
     */
    suspend operator fun invoke(conversationId: String): Flow<ApiResponse<Conversation>> {
        return conversationRepository.getConversation(User(id = authRepository.currentUserUid), conversationId)
    }
}