package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting the favorite listings from a given [userRepository] of an [authRepository]
 *
 * @param [userRepository] the repository which the use case will retrieve the favorite listing data from
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class GetFavoriteListings @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {

    /**
     * Retrieves all favorite listings from the [userRepository]
     */
    suspend operator fun invoke(): Flow<ApiResponse<List<Listing>>> {
        return userRepository.getFavoriteListings(authRepository.currentUserUid)
    }
}
