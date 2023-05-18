package com.studhub.app.data.local.entity;

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "unsent_message")
data class UnsentMessage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "message_id")
    val id: Int,
    val conversationId: String = "",
    val senderId: String = "",
    val content: String = "",
    val image: String? = null,
    val createdAt: Date = Date()
)
