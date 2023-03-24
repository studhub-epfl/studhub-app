package com.studhub.app.domain.usecase.listing

import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting the favorite listings from a given [listingRepository] of an [authRepository]
 *
 * @param [listingRepository] the repository which the use case will retrieve the listing data from
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class GetFavoriteListings @Inject constructor(private val listingRepository: ListingRepository, private val authRepository: AuthRepository) {

    /**
     * Retrieves all favorite listings from the [listingRepository]
     */
    suspend operator fun invoke(): Flow<ApiResponse<List<Listing>>> {
        return listingRepository.getFavoriteListings(authRepository.currentUserUid)
    }
}
