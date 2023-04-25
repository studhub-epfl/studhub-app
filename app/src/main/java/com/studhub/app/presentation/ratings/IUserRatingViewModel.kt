package com.studhub.app.presentation.ratings

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface IUserRatingViewModel {
    val currentUser: StateFlow<ApiResponse<User>>
    val ratings: StateFlow<ApiResponse<List<Rating>>>
    val targetUser: StateFlow<ApiResponse<User>>
    val currentUserLoading: StateFlow<Boolean>
    suspend fun initTargetUser(targetUserId: String)

    /**
     * adds a [rating] object to the user with ID userId
     */
    fun addRating(userId: String, rating: Rating)

    /**
     * updates the rating with ID [ratingId] of the user with ID [userId] with the [rating] object
     */
    fun updateRating(userId: String, ratingId: String, rating: Rating)

    /**
     * deletes a rating with ID [ratingId] from the user with ID userId
     */
    fun deleteRating(userId: String, ratingId: String)

    /**
     * fetches all the ratings corresponding to the user with ID userId
     */
    fun getUserRatings(userId: String)
    suspend fun getUserById(id: String): ApiResponse<User>

}
