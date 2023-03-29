package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for updating the current user with given profile information in [userRepository]
 *
 * @param [userRepository] the repository which the use case will act on
 * @param [authRepository] the repository which the use case will retrieve the logged in user from
 * @
 */
class UpdateCurrentUserInfo @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {

    /**
     * Updates the current user with profile information stored in [updatedUser]
     *
     * @param [userId] the ID of the user to update
     * @param [updatedUser] the user containing all the updated fields - the ID of this user will be discarded
     */
    suspend operator fun invoke(updatedUser: User): Flow<ApiResponse<User>> {
        return userRepository.updateUserInfo(authRepository.currentUserUid, updatedUser)
    }
}
