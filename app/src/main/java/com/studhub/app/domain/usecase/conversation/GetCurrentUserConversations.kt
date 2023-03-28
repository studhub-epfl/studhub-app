package com.studhub.app.domain.usecase.conversation

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all conversations of the connected user from the given [conversationRepository] and [authRepository]
 *
 * @param [conversationRepository] the repository which the use case will retrieve the conversations from
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class GetCurrentUserConversations @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val authRepository: AuthRepository
) {

    /**
     * Retrieves all conversations of the connected user
     */
    suspend operator fun invoke(): Flow<ApiResponse<List<Conversation>>> {
        val currentUser = User(id = authRepository.currentUserUid)
        return conversationRepository.getUserConversations(currentUser)
    }
}
