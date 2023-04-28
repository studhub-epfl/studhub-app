package com.studhub.app.domain.model

data class Listing(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val seller: User = User(),
    val price: Float = 0F,
    val categories: List<Category> = emptyList(),
    val meetingPoint: MeetingPoint?
)
