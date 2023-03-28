package com.studhub.app.domain.usecase.conversation

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.ConversationRepository
import com.studhub.app.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for sending a message to the given [messageRepository]
 *
 * @param [messageRepository] the repository which the use case will send the message to
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class SendMessage @Inject constructor(
    private val messageRepository: MessageRepository,
    private val conversationRepository: ConversationRepository,
    private val authRepository: AuthRepository
) {

    /**
     * Sends a message.
     */
    suspend operator fun invoke(
        conversation: Conversation,
        message: Message
    ): Flow<ApiResponse<Message>> {
        val msg =
            message.copy(senderId = authRepository.currentUserUid, conversationId = conversation.id)

        return flow {
            messageRepository.createMessage(msg).collect { msgQuery ->
                when (msgQuery) {
                    is ApiResponse.Success -> {
                        conversationRepository.updateLastMessageWith(conversation, msgQuery.data)
                            .collect { convoQuery ->
                                when (convoQuery) {
                                    is ApiResponse.Failure -> emit(ApiResponse.Failure(convoQuery.message))
                                    is ApiResponse.Loading -> emit(ApiResponse.Loading)
                                    is ApiResponse.Success -> emit(msgQuery)
                                }
                            }
                    }
                    else -> emit(msgQuery)
                }
            }
        }
    }
}
