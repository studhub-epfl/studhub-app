package com.studhub.app.domain.model

data class User(
        var id: String = "",
        var email: String = "",
        var phoneNumber: String = "",
        var firstName: String = "",
        var lastName: String = "",
        var userName: String = "",
        var profilePicture: String = ""
)
