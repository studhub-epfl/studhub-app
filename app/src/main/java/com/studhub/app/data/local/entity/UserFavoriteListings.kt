package com.studhub.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user_fav_listings", primaryKeys = ["user_id", "listing_id"])
data class UserFavoriteListings(
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "listing_id")
    val listingId: String
)
