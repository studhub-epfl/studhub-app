package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for updating a favorite listing from a given [ListingRepository]
 *
 * @param [listingRepository] the repository which the use case will retrieve the listing data from
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class UpdateFavoriteListingStatus (private val listingRepository: ListingRepository, private val authRepository: AuthRepository) {

    /**
     * Updates the favorite status of the listing with ID [favListingId] from the [authRepository]
     *
     * @param [favListingId] the ID of the listing to update
     * @param [isFavorite] the new favorite status for the listing
     */
    suspend operator fun invoke(favListingId: String, isFavorite: Boolean): Flow<ApiResponse<Listing>> {
        return listingRepository.updateFavoriteListingStatus(authRepository.currentUserUid, favListingId, isFavorite)
    }
}
