package com.studhub.app.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "conversation")
data class Conversation(
    @PrimaryKey
    @ColumnInfo(name = "conversation_id")
    var id: String = "",
    var user1Id: String = "",
    var user2Id: String = "",
    var user1Name: String = "",
    var user2Name: String = "",
    @Ignore
    var user1: User? = null,
    @Ignore
    var user2: User? = null,
    var createdAt: Date = Date(),
    var updatedAt: Date = Date(),
    var lastMessageContent: String = ""
) {
    constructor() : this("")
}
