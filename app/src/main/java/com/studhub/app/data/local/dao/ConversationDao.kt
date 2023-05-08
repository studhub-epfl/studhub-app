package com.studhub.app.data.local.dao

import androidx.room.*
import com.studhub.app.domain.model.Conversation

@Dao
interface ConversationDao {
    @Transaction
    @Query("SELECT * FROM conversation WHERE user1Id = :userId OR user2Id = :userId ORDER BY updatedAt DESC")
    suspend fun getConversations(userId: String): List<Conversation>

    @Query("SELECT * FROM conversation WHERE conversation_id = :conversationId")
    suspend fun getConversation(conversationId: String): Conversation

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation)
}
