package com.studhub.app.domain.model

import android.net.Uri

data class Listing(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val seller: User = User(),
    val price: Float = 0F,
    val categories: List<Category> = emptyList(),
    val meetingPoint: MeetingPoint? = MeetingPoint(0.0,0.0)
    val pictures: List<String> = emptyList(),
    val picturesUri: List<Uri>? = null
)
