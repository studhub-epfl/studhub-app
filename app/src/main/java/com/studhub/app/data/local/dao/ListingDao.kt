package com.studhub.app.data.local.dao

import androidx.room.*
import com.studhub.app.domain.model.Listing

@Dao
interface ListingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: Listing)

    @Query("DELETE FROM listing WHERE listing_id = :id")
    suspend fun deleteListing(id: String)
}
