package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for creating a user in a given [repository]
 *
 * @param [repository] the repository which the use case will act on
 */
class CreateUser @Inject constructor(private val repository: UserRepository) {

    /**
     * Adds the given [user] to a [repository]
     *
     * @param [user] the user to create
     */
    suspend operator fun invoke(user: User): Flow<ApiResponse<User>> {
        return repository.createUser(user)
    }
}
