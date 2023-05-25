package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.util.*

interface ListingRepository {
    /**
     * Create a [Listing] in the repository
     * @param listing the listing we want to create
     * @return A [Flow] of [ApiResponse] with the last one containing the [Listing] pushed to the database on success
     */
    suspend fun createListing(listing: Listing): Flow<ApiResponse<Listing>>

    /**
     * Creates a draft [Listing]
     * @param listing the draft listing we want to save
     * @return A [Flow] of [ApiResponse] with the last one containing the saved [Listing] on success
     */
    suspend fun saveDraftListing(listing: Listing): Flow<ApiResponse<Listing>>

    /**
     * Retrieves a draft [Listing] from the repository
     * @param listingId the if of the draft listing we want to retrieve
     * @return A [Flow] of [ApiResponse] with the last one containing the saved [Listing] on success or null
     */
    suspend fun getDraftListing(listingId: String): Flow<ApiResponse<Listing?>>

    /**
     * Get a [List] of all the [Listing] in the repository
     * @return A [Flow] of [ApiResponse] with the last one containing the list of [Listing] retrieved from the repository on success
     */
    suspend fun getListings(): Flow<ApiResponse<List<Listing>>>

    /**
     * Get the [Listing] with the given [listingId] from the repository
     * @param [listingId] the ID of the listing we want to match
     * @return A [Flow] of [ApiResponse] with the last one containing the [Listing] retrieved from the repository on success
     */
    suspend fun getListing(listingId: String): Flow<ApiResponse<Listing>>

    /**
     * Get the [List] of [Listing] created by the given [user]
     * @return A [Flow] of [ApiResponse] with the last one containing the list of [Listing] retrieved from the repository on success
     */
    suspend fun getUserListings(user: User): Flow<ApiResponse<List<Listing>>>

    /**
     * Get the [List] of draft [Listing] saved by the given [user]
     * @return A [Flow] of [ApiResponse] with the last one containing the list of draft [Listing] on success
     */
    suspend fun getUserDraftListings(user: User): Flow<ApiResponse<List<Listing>>>

    /**
     * Get a [List] of [Listing] with all the listings on the database of Firebase with the constraint given on parameter
     * @param [keyword] the constraint we want the listings to filter
     * @param [blockedUsers] the list of users that we don't want to see their listings
     * @return A [Flow] of [ApiResponse] with the last one containing the filtered list of [Listing] pushed to the database on success
     */
    suspend fun getListingsBySearch(
        keyword: String,
        minPrice: String,
        maxPrice: String,
        categoryChoose: List<Category>,
        blockedUsers: Map<String, Boolean>
    ): Flow<ApiResponse<List<Listing>>>

    /**
     * Updates the [Listing] with the given [listingId]
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
    suspend fun updateListingToBidding(
        listing: Listing,
        startingPrice: Float,
        deadline: Date
    ): Flow<ApiResponse<Listing>>
}
