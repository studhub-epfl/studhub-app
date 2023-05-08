package com.studhub.app.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "message")
data class Message(
    @PrimaryKey
    @ColumnInfo(name = "message_id")
    var id: String = "",
    var conversationId: String = "",
    @Ignore
    var conversation: Conversation? = null,
    var senderId: String = "",
    @Ignore
    var sender: User? = null,
    var content: String = "",
    var image: String? = null,
    var createdAt: Date = Date()
) {
    constructor() : this("")
}
