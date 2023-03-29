package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for signing out user from the given [authRepository]
 *
 * @param [authRepository] the repository which the use case will sign out from
 */
class SignOut @Inject constructor(private val authRepository: AuthRepository) {

    suspend operator fun invoke(): Flow<ApiResponse<Boolean>> {
        return authRepository.signOut()
    }
}
