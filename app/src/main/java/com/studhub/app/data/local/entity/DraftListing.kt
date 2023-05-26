package com.studhub.app.data.local.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.studhub.app.domain.model.ListingType
import java.util.*

@Entity(tableName = "draft_listing")
data class DraftListing(
    @PrimaryKey
    @ColumnInfo(name = "draft_listing_id")
    val id: String,
    val name: String = "",
    val description: String = "",
    val sellerId: String = "",
    val lastModifiedAt: Date = Date(),
    val price: Float = 0F,
    val picturesUri: List<Uri>,
    val type: ListingType = ListingType.FIXED,
    val biddingDeadline: Date = Date()
)
