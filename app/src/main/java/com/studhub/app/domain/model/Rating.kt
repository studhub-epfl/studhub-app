package com.studhub.app.domain.model

data class Rating(
    val id: String = "",
    val userId: String = "",
    val reviewerId: String = "",
    val thumbUp: Boolean = false,
    val thumbDown: Boolean = false,
    val comment: String = ""
)
