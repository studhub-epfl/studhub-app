package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for updating a user from a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 */
class UpdateUser(private val repository: UserRepository) {

    /**
     * Updates the user with ID [userId] from the [repository]
     *
     * The ID of the given [updatedUser] shall be discarded
     *
     * @param [userId] the ID of the user to update
     * @param [updatedUser] the user containing all the updated fields - the ID of this user will be discarded
     */
    suspend operator fun invoke(userId: Long, updatedUser: User): Flow<ApiResponse<User>> {
        return repository.updateUser(userId, updatedUser)
    }
}