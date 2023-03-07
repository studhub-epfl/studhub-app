package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting a user from a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 */
class GetUser(private val repository: UserRepository) {

    /**
     * Retrieves the user matching the given [userId] from the [repository]
     *
     * @param [userId] the ID of the user to retrieve
     */
    suspend operator fun invoke(userId: Long): Flow<ApiResponse<User>> {
        return repository.getUser(userId)
    }
}
