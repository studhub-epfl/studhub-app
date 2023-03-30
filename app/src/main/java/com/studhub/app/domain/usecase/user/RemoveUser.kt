package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for removing a user from a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 */
class RemoveUser @Inject constructor(private val repository: UserRepository) {

    /**
     * Removes the user matching the given [userId] from the [repository]
     *
     * @param [userId] the ID of the user to remove
     */
    suspend operator fun invoke(userId: String): Flow<ApiResponse<Boolean>> {
        return repository.removeUser(userId)
    }
}
