package com.studhub.app.domain.model

import java.util.*

data class Conversation(
    val id: String = "",
    val user1Id: String = "",
    val user2Id: String = "",
    val user1: User? = null,
    val user2: User? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
