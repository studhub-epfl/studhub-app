package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting the blocked users from a given [userRepository] of an [authRepository]
 *
 * @param [userRepository] the repository which the use case will retrieve the blocked user data from
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class GetBlockedUsers @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {

    /**
     * Retrieves all blocked users from the [userRepository]
     */
    suspend operator fun invoke(): Flow<ApiResponse<List<User>>> {
        return userRepository.getBlockedUsers(authRepository.currentUserUid)
    }
}
