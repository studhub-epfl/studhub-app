package com.studhub.app.domain.model

data class User(
        var id: Long = 0L,
        var email: String = "",
        var phoneNumber: String = "",
        var firstName: String = "",
        var lastName: String = "",
        var userName: String = "",
        var profilePicture: String = ""
)
