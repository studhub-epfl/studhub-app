package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for removing a listing from a given [UserRepository]
 *
 * @param [userRepository] the repository which the use case will retrieve the the listing id from
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class RemoveFavoriteListing @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {

    /**
     * Removes the listing with ID [favListingId] from the favorite listings from [userRepository]
     *
     * @param [favListingId] the ID of the listing to remove from the favorite listings
     */
    suspend operator fun invoke(favListingId: String): Flow<ApiResponse<User>> {
        return userRepository.removeFavoriteListing(
            authRepository.currentUserUid,
            favListingId
        )
    }
}
