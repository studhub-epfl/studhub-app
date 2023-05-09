package com.studhub.app.domain.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    var id: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var userName: String = "",
    var profilePicture: String = "",
    @Ignore
    var profilePictureUri: Uri? = null,
    var favoriteListings: Map<String, Boolean> = emptyMap(),
    var blockedUsers: Map<String, Boolean> = emptyMap(),
    @Ignore
    var ratings: Map<String, Rating> = emptyMap(),
    var thumbsUpCount: Int = 0,
    var thumbsDownCount: Int = 0
) {
    constructor() : this("")
}
