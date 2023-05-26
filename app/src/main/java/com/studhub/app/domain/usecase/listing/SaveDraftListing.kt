package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for saving a draft [Listing] in a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class SaveDraftListing @Inject constructor(
    private val repository: ListingRepository,
    private val authRepository: AuthRepository
) {
    /**
     * Adds the given draft [listing] to a [repository]
     *
     * The seller will be the [User] logged-in to the [authRepository]
     *
     * @param [listing] the draft [Listing] to save
     */
    suspend operator fun invoke(listing: Listing): Flow<ApiResponse<Listing>> {
        return repository.saveDraftListing(listing.copy(sellerId = authRepository.currentUserUid))
    }
}
