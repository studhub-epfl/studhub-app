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

    // Override equals to compare the contents of the objects.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Conversation

        if (id != other.id) return false
        if (user1Id != other.user1Id) return false
        if (user2Id != other.user2Id) return false
        if (user1Name != other.user1Name) return false
        if (user2Name != other.user2Name) return false
        if (user1 != other.user1) return false
        if (user2 != other.user2) return false
        if (lastMessageContent != other.lastMessageContent) return false

        return true
    }

    // Override hashCode to compute the hash based on the contents of the object.
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + user1Id.hashCode()
        result = 31 * result + user2Id.hashCode()
        result = 31 * result + user1Name.hashCode()
        result = 31 * result + user2Name.hashCode()
        result = 31 * result + (user1?.hashCode() ?: 0)
        result = 31 * result + (user2?.hashCode() ?: 0)
        result = 31 * result + lastMessageContent.hashCode()
        return result
    }


    fun Date.isCloseTo(other: Date, toleranceMillis: Long = 1000): Boolean {
        return Math.abs(this.time - other.time) <= toleranceMillis
    }
}
