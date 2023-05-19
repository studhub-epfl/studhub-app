package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all draft listings from the logged-in user, from a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class GetOwnDraftListings @Inject constructor(
    private val repository: ListingRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Flow<ApiResponse<List<Listing>>> {
        return repository.getUserDraftListings(User(id = authRepository.currentUserUid))
    }
}
