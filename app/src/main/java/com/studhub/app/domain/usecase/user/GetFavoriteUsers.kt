package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting all the favorite users from a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 */
class GetFavoriteUsers(private val repository: UserRepository) {

    /**
     * Retrieves all favorite users from the [repository]
     */
    suspend operator fun invoke(): Flow<ApiResponse<List<User>>> {
        return repository.getFavoriteUsers()
    }
}

