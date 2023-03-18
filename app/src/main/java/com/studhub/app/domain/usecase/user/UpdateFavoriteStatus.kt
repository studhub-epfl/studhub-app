package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for updating a favorite user from a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 */
class UpdateFavoriteStatus(private val repository: UserRepository) {

    /**
     * Updates the favorite status of the user with ID [userId] from the [repository]
     *
     * @param [userId] the ID of the user to update
     * @param [isFavorite] the new favorite status for the user
     */
    suspend operator fun invoke(userId: String, isFavorite: Boolean): Flow<ApiResponse<User>> {
        return repository.updateFavoriteStatus(userId, isFavorite)
    }
}
