package com.studhub.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.studhub.app.data.local.entity.DraftListing

@Dao
interface DraftListingDao {
    @Query("SELECT * FROM draft_listing WHERE sellerId = :sellerId ORDER BY lastModifiedAt DESC")
    suspend fun getDraftListings(sellerId: String): List<DraftListing>

    @Query("SELECT * FROM draft_listing WHERE draft_listing_id = :listingId")
    suspend fun getDraftListing(listingId: String): DraftListing?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraftListing(draftListing: DraftListing)

    @Query("DELETE FROM draft_listing WHERE draft_listing_id = :draftListingId")
    suspend fun deleteDraftListing(draftListingId: String)
}
