package com.studhub.app.domain.usecase.conversation

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for sending a message to the given [messageRepository]
 *
 * @param [messageRepository] the repository which the use case will send the message to
 */
class SendMessage @Inject constructor(private val messageRepository: MessageRepository) {

    /**
     * Sends a message.
     */
    suspend operator fun invoke(message: Message): Flow<ApiResponse<Message>> {
        return messageRepository.createMessage(message)
    }
}
