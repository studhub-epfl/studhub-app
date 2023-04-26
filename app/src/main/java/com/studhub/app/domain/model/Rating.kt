package com.studhub.app.domain.model

data class Rating(
    val id: String = "",
    val userId: String = "",
    val reviewerId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val comment: String = "",
    val timestamp: Long = 0,
    val thumbUp: Boolean = false,
    val thumbDown: Boolean = false
)
