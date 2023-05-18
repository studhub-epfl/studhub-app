package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import kotlinx.coroutines.flow.Flow
import java.util.*

interface ListingRepository {
    /**
     * create a [listing] on the database of Firebase
     * @param [listing] the listing we want to create
     * @return A [Flow] of [ApiResponse] with the last one containing the [Listing] pushed to the database on success
     */
    suspend fun createListing(listing: Listing): Flow<ApiResponse<Listing>>

    /**
     * get a list of [listing] with all the listings on the database of Firebase
     * @return A [Flow] of [ApiResponse] with the last one containing the list of [Listing] pushed to the database on success
     */
    suspend fun getListings(): Flow<ApiResponse<List<Listing>>>

    /**
     * get a  [listing] with all the listings on the database of Firebase
     * @param [listingId] the listingId we want to match
     * @return A [Flow] of [ApiResponse] with the last one containing the [Listing] pushed to the database on success
     */
    suspend fun getListing(listingId: String): Flow<ApiResponse<Listing>>

    /**
     * get a list of [listing] with all the listings on the database of Firebase with the constraint given on parameter
     * @param [keyword] the constraint we want the listings to filter
     * @param [blockedUsers] the list of users that we don't want to see their listings
     * @return A [Flow] of [ApiResponse] with the last one containing the filtered list of [Listing] pushed to the database on success
     */
    suspend fun getListingsBySearch(keyword: String, blockedUsers: Map<String, Boolean>): Flow<ApiResponse<List<Listing>>>

    /**
     * get a list of [listing] with all the listings on the database of Firebase with the price constraints given on parameter
     * @param [keyword1] the minimal price constraint
     * @param [keyword2] the maximal price constraint
     * @return A [Flow] of [ApiResponse] with the last one containing the filtered list of [Listing] pushed to the database on success
     */
    suspend fun getListingsByRange(keyword: String, keyword2: String): Flow<ApiResponse<List<Listing>>>


    /**
     * update a listing with the given [listingId]
     * @param [listingId] the listingId we want to match
     * @param [updatedListing] the Listing we want to replace
     * @return A [Flow] of [ApiResponse] with the last one containing the updated [Listing] pushed to the database on success
     */
    suspend fun updateListing(
        listingId: String,
        updatedListing: Listing
    ): Flow<ApiResponse<Listing>>

    /**
     * remove a listing with the given [listingId]
     * @param [listingId] the listingId we want to match
     * @return A [Flow] of [ApiResponse] with the last one containing the [Boolean] value of the resulting operation
     */
    suspend fun removeListing(listingId: String): Flow<ApiResponse<Boolean>>

    /**
     * update a [Listing] to be of bidding type
     * @param [listing] the listing to be updated
     * @param [startingPrice] the starting price for the bidding type
     * @param [deadline] the deadline for the fixing the bidding price
     * @return A [Flow] of [ApiResponse] with the last one containing the updated [Listing] pushed to the database on success
     */
    suspend fun updateListingToBidding(listing: Listing, startingPrice: Float, deadline: Date): Flow<ApiResponse<Listing>>

    /**
     * Subscribes the user to the given listing.
     * @param userId The ID of the user.
     * @param listingId The ID of the listing.
     */
    suspend fun subscribeToListing(userId: String, listingId: String): Flow<ApiResponse<Boolean>>

    /**
     * Unsubscribes the user from the given listing.
     * @param userId The ID of the user.
     * @param listingId The ID of the listing.
     */
    suspend fun unsubscribeFromListing(userId: String, listingId: String): Flow<ApiResponse<Boolean>>

}
