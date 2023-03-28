package com.studhub.app.domain.model

import java.util.*

data class Message(
    val id: String = "",
    val conversationId: String = "",
    val conversation: Conversation? = null,
    val senderId: String = "",
    val sender: User? = null,
    val content: String = "",
    val image: String? = null,
    val createdAt: Date = Date()
)
