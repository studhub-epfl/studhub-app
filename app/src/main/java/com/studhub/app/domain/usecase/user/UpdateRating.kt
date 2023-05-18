package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateRating @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String, ratingId: String, rating: Rating): Flow<ApiResponse<Rating>> {
        return repository.updateRating(userId, ratingId, rating)
    }
}
