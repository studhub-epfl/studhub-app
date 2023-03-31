package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for adding a favorite listing to a given [UserRepository]
 *
 * @param [userRepository] the repository which the use case will retrieve the the listing id from
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class AddFavoriteListing @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {

    /**
     * Adds the favorite feature to the listing with ID [favListingId] from the [authRepository]
     *
     * @param [favListingId] the ID of the listing to add to the favorite listing
     */
    suspend operator fun invoke(favListingId: String): Flow<ApiResponse<User>> {
        return userRepository.addFavoriteListing(
            authRepository.currentUserUid,
            favListingId
        )
    }
}
