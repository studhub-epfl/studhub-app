package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject


/**
 * Use case for updating a listing to a bidding type in a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 */
class UpdateListingToBidding @Inject constructor(private val repository: ListingRepository) {

    /**
     * Updates the listing [listing] in the [repository] with new properties [startingPrice] and [deadline]
     *
     * @param [listing] the listing to update
     * @param [startingPrice] the starting price for the bidding type
     * @param [deadline] the deadline for the fixing the bidding price
     */
    suspend operator fun invoke(
        listing: Listing, startingPrice: Float, deadline: Date
    ): Flow<ApiResponse<Listing>> {
        return repository.updateListingToBidding(listing, startingPrice, deadline)
    }
}
