package com.studhub.app.data.local.dao

import androidx.room.*
import com.studhub.app.domain.model.Listing

@Dao
interface ListingDao {
    @Transaction
    @Query("SELECT * FROM listing")
    suspend fun getListings(): List<Listing>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: Listing)
}
