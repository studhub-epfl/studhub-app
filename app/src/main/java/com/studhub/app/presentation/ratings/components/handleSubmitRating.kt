package com.studhub.app.presentation.ratings.components

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.ratings.IUserRatingViewModel
import java.util.*


fun handleSubmitRating(
    viewModel: IUserRatingViewModel,
    targetUserId: String,
    currentUser: State<ApiResponse<User>>,
    thumbUp: Boolean,
    ratingText: String,
    currentRating: Rating?,
    thumbsUpCount: MutableState<Int>,
    thumbsDownCount: MutableState<Int>,
    thumbDown: Boolean
) {
    currentUser.value.let { userResponse ->
        if (userResponse is ApiResponse.Success) {
            if (currentRating == null) {
                val UUID = UUID.randomUUID().toString()
                viewModel.addRating(
                    targetUserId,
                    Rating(
                        id = UUID,
                        userId = targetUserId,
                        reviewerId = userResponse.data.id,
                        firstName = userResponse.data.firstName,
                        lastName = userResponse.data.lastName,
                        thumbUp = thumbUp,
                        thumbDown = !thumbUp,
                        comment = ratingText,
                        timestamp = System.currentTimeMillis()
                    )
                )
                thumbsUpCount.value += if (thumbUp) 1 else 0
                thumbsDownCount.value += if (thumbDown) 1 else 0
            } else {
                viewModel.updateRating(
                    targetUserId,
                    currentRating.id,
                    currentRating.copy(
                        thumbUp = thumbUp,
                        thumbDown = !thumbUp,
                        comment = ratingText
                    )
                )
                thumbsUpCount.value += if (thumbUp) 1 else 0
                thumbsDownCount.value += if (thumbDown) 1 else 0
            }
            viewModel.getUserRatings(targetUserId)
        }
    }
}
