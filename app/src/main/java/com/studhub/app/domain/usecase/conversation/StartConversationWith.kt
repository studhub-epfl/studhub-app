package com.studhub.app.domain.usecase.conversation

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for creating a conversation between the logged in user and another user in the given [conversationRepository]
 *
 * @param [conversationRepository] the repository which the use case will create the conversation in
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class StartConversationWith @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val authRepository: AuthRepository
) {

    /**
     * Creates a conversation with the given user.
     */
    suspend operator fun invoke(user: User): Flow<ApiResponse<Conversation>> {
        val conversation = Conversation(user1Id = authRepository.currentUserUid, user2Id = user.id)
        return conversationRepository.createConversation(conversation)
    }
}
