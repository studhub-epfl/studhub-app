package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting the connected user from the given [userRepository] and [authRepository]
 *
 * @param [userRepository] the repository which the use case will retrieve the user data from
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class GetCurrentUser @Inject constructor(private val userRepository: UserRepository, private val authRepository: AuthRepository) : IGetCurrentUser {

    /**
     * Retrieves the connected user from
     */
    override suspend operator fun invoke(): Flow<ApiResponse<User>> {
        return userRepository.getUser(authRepository.currentUserUid)
    }
}
