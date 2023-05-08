package com.studhub.app.data.local.dao

import androidx.room.*
import com.studhub.app.domain.model.Conversation

@Dao
interface ConversationDao {
    @Transaction
    @Query("SELECT * FROM conversation WHERE user1Id = :userId OR user2Id = :userId")
    suspend fun getConversations(userId: String): List<Conversation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation)
}
