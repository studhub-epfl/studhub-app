package com.studhub.app.data.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockConversationRepositoryImpl : ConversationRepository {
    override suspend fun createConversation(conversation: Conversation): Flow<ApiResponse<Conversation>> {
        return flowOf(ApiResponse.Success(Conversation()))
    }

    override suspend fun getConversation(
        user: User,
        conversationId: String
    ): Flow<ApiResponse<Conversation>> {
        return flowOf(ApiResponse.Success(Conversation()))
    }

    override suspend fun getUserConversations(user: User): Flow<ApiResponse<List<Conversation>>> {
        return flowOf(ApiResponse.Success(listOf(Conversation())))
    }
}
