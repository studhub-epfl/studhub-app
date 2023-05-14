package com.studhub.app.data.local.dao

import androidx.room.*
import com.studhub.app.data.local.entity.UnsentMessage

@Dao
interface UnsentMessageDao {
    @Query("SELECT * FROM unsent_message WHERE conversationId = :conversationId ORDER BY createdAt ASC")
    suspend fun getUnsentMessages(conversationId: String): List<UnsentMessage>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUnsentMessage(unsentMessage: UnsentMessage)

    @Query("DELETE FROM unsent_message WHERE senderId = :senderId")
    suspend fun deleteUnsentMessages(senderId: String)
}
