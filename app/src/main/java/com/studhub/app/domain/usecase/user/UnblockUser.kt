package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for unblocking a user from a given [UserRepository]
 *
 * @param [userRepository] the repository which the use case will retrieve the the blocked user id from
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 */
class UnblockUser @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {

    /**
     * Removes the blocked user with ID [blockedUserId] from the logged in user's blocked list
     *
     * @param [blockedUserId] the ID of the user to remove from the blocked list
     */
    suspend operator fun invoke(blockedUserId: String): Flow<ApiResponse<User>> {
        return userRepository.unblockUser(
            authRepository.currentUserUid,
            blockedUserId
        )
    }
}
