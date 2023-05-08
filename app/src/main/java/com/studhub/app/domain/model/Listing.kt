package com.studhub.app.domain.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "listing")
data class Listing(
    @PrimaryKey
    @ColumnInfo(name = "listing_id")
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var sellerId: String = "",
    var createdAt: Date = Date(),
    @Ignore
    var seller: User = User(),
    var price: Float = 0F,
    @Ignore
    var categories: List<Category> = emptyList(),
    @Ignore
    var meetingPoint: MeetingPoint? = MeetingPoint(0.0,0.0),
    var pictures: List<String> = emptyList(),
    @Ignore
    var picturesUri: List<Uri>? = null
) {
    constructor() : this("")
}
