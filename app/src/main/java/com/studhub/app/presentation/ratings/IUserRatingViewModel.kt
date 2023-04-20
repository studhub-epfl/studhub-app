package com.studhub.app.presentation.ratings

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface IUserRatingViewModel {
    val currentUser: StateFlow<ApiResponse<User>>
    val ratings: StateFlow<ApiResponse<List<Rating>>>
    val targetUser : StateFlow<ApiResponse<User>>
    fun initTargetUser(targetUserId: String)
    fun addRating(userId: String, rating: Rating)
    fun updateRating(userId: String, ratingId: String, rating: Rating)
    fun deleteRating(userId: String, ratingId: String)
    fun getUserRatings(userId: String)
    suspend fun getUserById(id: String): ApiResponse<User>

}
