package com.studhub.app.data.local.dao

import androidx.room.*
import com.studhub.app.data.local.entity.UserFavoriteListings
import com.studhub.app.domain.model.Listing

@Dao
interface UserFavListingsDao {
    @Transaction
    @Query(
        "SELECT * FROM listing AS l " +
                "INNER JOIN user_fav_listings AS f ON l.listing_id = f.listing_id " +
                "WHERE f.user_id = :userId"
    )
    suspend fun getUserFavorites(userId: String): List<Listing>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteListing(userFavoriteListings: UserFavoriteListings)

    @Query("DELETE FROM user_fav_listings WHERE user_id = :userId AND listing_id = :listingId")
    suspend fun deleteFavoriteListing(userId: String, listingId: String)
}
