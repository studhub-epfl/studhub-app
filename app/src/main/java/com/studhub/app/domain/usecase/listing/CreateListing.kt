package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for creating a listing in a given [repository]
 *
 * @param [repository] the which the use case will act on
 */
class CreateListing @Inject constructor(
    private val repository: ListingRepository,
    private val authRepository: AuthRepository
) {

    /**
     * Adds the given [listing] to a [repository]
     * The seller will be the [User] logged-in to the [authRepository]
     *
     * @param [listing] the listing to create
     */
    suspend operator fun invoke(listing: Listing): Flow<ApiResponse<Listing>> {
        return repository.createListing(listing.copy(seller = User(id = authRepository.currentUserUid)))
    }
}
