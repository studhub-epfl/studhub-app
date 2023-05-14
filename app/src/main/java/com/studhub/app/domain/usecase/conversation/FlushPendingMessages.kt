package com.studhub.app.domain.usecase.conversation

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for sending all pending messages to the given [messageRepository]
 *
 * @param [messageRepository] the repository which the use case will send the messages to
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class FlushPendingMessages @Inject constructor(
    private val messageRepository: MessageRepository,
    private val authRepository: AuthRepository
) {
    /**
     * Sends all pending messages.
     */
    suspend operator fun invoke(): Flow<ApiResponse<Boolean>> {
        return messageRepository.flushPendingMessages(User(id = authRepository.currentUserUid))
    }
}
