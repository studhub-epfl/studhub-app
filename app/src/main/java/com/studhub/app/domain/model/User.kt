package com.studhub.app.domain.model

import android.net.Uri

data class User(
    val id: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val userName: String = "",
    val profilePicture: String = "",
    val profilePictureUri: Uri? = null,
    val favoriteListings: Map<String, Boolean> = emptyMap(),
    val blockedUsers: Map<String, Boolean> = emptyMap(),
)
