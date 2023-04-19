package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteRating @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String, ratingId: String): Flow<ApiResponse<Boolean>> {
        return repository.deleteRating(userId, ratingId)
    }
}
