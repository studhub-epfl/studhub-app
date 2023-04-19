package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddRating @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String, rating: Rating): Flow<ApiResponse<Rating>> {
        return repository.addRating(userId, rating)
    }
}
