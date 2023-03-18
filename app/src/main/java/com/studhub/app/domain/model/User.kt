package com.studhub.app.domain.model

data class User(
    val id: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val userName: String = "",
    val profilePicture: String = "",
    var isFavorite: Boolean = false,
)
