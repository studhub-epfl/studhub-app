package com.studhub.app.presentation.ratings

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.presentation.ratings.components.*
import java.util.*


@Composable
fun UserRatingScreen(
    targetUserId: String,
    viewModel: IUserRatingViewModel = hiltViewModel<UserRatingViewModel>()
) {


    val ratings = viewModel.ratings.collectAsState(initial = ApiResponse.Loading)
    val currentUserLoading = viewModel.currentUserLoading.collectAsState(initial = true)
    val currentUser = viewModel.currentUser.collectAsState(initial = ApiResponse.Loading)
    var showDialog by remember { mutableStateOf(false) }
    var currentRating: Rating? by remember { mutableStateOf(null) }
    var ratingText by remember { mutableStateOf("") }
    var thumbUp by remember { mutableStateOf(false) }
    var thumbDown by remember { mutableStateOf(false) }
    val targetUser = viewModel.targetUser.collectAsState(initial = ApiResponse.Loading)
    val buttonEnabled = remember { mutableStateOf(false) }
    val thumbsUpCount = remember { mutableStateOf(0) }
    val thumbsDownCount = remember { mutableStateOf(0) }



    LaunchedEffect(currentUser.value, currentUserLoading.value) {
        buttonEnabled.value = currentUser.value is ApiResponse.Success && !currentUserLoading.value
    }


    LaunchedEffect(targetUser.value) {
        viewModel.initTargetUser(targetUserId)
        if (targetUser.value is ApiResponse.Success) {
            viewModel.getUserRatings(targetUserId)
        }
    }

    var currentUserHasRating by remember { mutableStateOf(false) }

    LaunchedEffect(ratings.value) {

        if (ratings.value is ApiResponse.Success) {
            val currentUserData = currentUser.value
            if (currentUserData is ApiResponse.Success) {
                val ratingList = (ratings.value as ApiResponse.Success).data
                currentUserHasRating =
                    ratingList.any { it.reviewerId == currentUserData.data.id }
                thumbsUpCount.value = ratingList.count { it.thumbUp }
                thumbsDownCount.value = ratingList.count { it.thumbDown }
            }
        }
    }

    RatingDialog(
        showDialog = showDialog,
        setShowDialog = { showDialog = false },
        onSubmit = { thumbUp, ratingText ->
            handleSubmitRating(
                viewModel,
                targetUserId,
                currentUser,
                thumbUp,
                ratingText,
                currentRating,
                thumbsUpCount,
                thumbsDownCount,
                thumbDown
            )
        },
        thumbUp = thumbUp,
        setThumbUp = { thumbUp = it },
        ratingText = ratingText,
        setRatingText = { ratingText = it },
        currentUser = currentUser,
        currentUserLoading = currentUserLoading
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        UserHeader(
            targetUser = targetUser,
            thumbsUpCount = thumbsUpCount.value,
            thumbsDownCount = thumbsDownCount.value
        )

        Spacer(modifier = Modifier.height(16.dp))

        AddRatingButton(
            onShowDialogToggle = { showDialog = !showDialog },
            currentUser = currentUser,
            currentUserLoading = currentUserLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        RatingsList(ratings = ratings,
            currentUser = currentUser,
            currentUserHasRating = currentUserHasRating,
            onEditRating = { rating ->
                // Show dialog for editing a rating
                currentRating = rating
                ratingText = rating.comment
                thumbUp = rating.thumbUp
                thumbDown = !thumbUp
                showDialog = true
            },
            onRemoveRating = { rating ->
                viewModel.deleteRating(
                    rating.userId,
                    rating.id
                )
                thumbsUpCount.value -= if (thumbUp) 1 else 0
                thumbsDownCount.value -= if (thumbDown) 1 else 0
            })
    }
}



