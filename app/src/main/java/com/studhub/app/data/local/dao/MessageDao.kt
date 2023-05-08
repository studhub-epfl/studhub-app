package com.studhub.app.data.local.dao

import androidx.room.*
import com.studhub.app.domain.model.Message

@Dao
interface MessageDao {
    @Transaction
    @Query("SELECT * FROM message WHERE conversationId = :conversationId ORDER BY createdAt ASC")
    suspend fun getMessages(conversationId: String): List<Message>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)
}
