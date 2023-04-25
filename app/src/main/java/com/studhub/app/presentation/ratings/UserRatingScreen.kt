package com.studhub.app.presentation.ratings

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.ratings.components.RatingItem
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
        Log.d("UserRatingScreen", "LaunchedEffect ratings.value: ${ratings.value}")

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



    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(modifier = Modifier.size(300.dp, 200.dp)) {
                Column {
                    Text("Thumbs Up / Thumbs Down")
                    Row {
                        Checkbox(checked = thumbUp, onCheckedChange = { thumbUp = it })
                        Checkbox(checked = !thumbUp, onCheckedChange = { thumbUp = !it })
                    }
                    TextField(value = ratingText, onValueChange = { ratingText = it })
                    Button(
                        onClick = {
                            Log.d("UserRatingScreen", "Submit button clicked")
                            currentUser.value.let { userResponse ->
                                if (userResponse is ApiResponse.Success) {
                                    if (currentRating == null) {
                                        Log.d("UserRatingScreen", "Add rating triggered 1")
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
                                        Log.d("UserRatingScreen", "Update rating triggered 1")
                                        viewModel.updateRating(
                                            targetUserId,
                                            currentRating!!.id,
                                            currentRating!!.copy(
                                                thumbUp = thumbUp,
                                                thumbDown = !thumbUp,
                                                comment = ratingText
                                            )
                                        )
                                        thumbsUpCount.value += if (thumbUp) 1 else 0
                                        thumbsDownCount.value += if (thumbDown) 1 else 0
                                    }
                                    showDialog = false
                                    Log.d("UserRatingScreen", "False reached $showDialog")
                                    viewModel.getUserRatings(targetUserId)
                                    Log.d("UserRatingScreen", "False Got ratings $showDialog")
                                } else {
                                    Log.d("UserRatingScreen", "Failed")
                                }
                            }
                        },
                        content = { Text("Submit") },
                        enabled = currentUser.value is ApiResponse.Success && !currentUserLoading.value
                    )
                }
            }
        }
    }

    Column {
        when (val userResponse = targetUser.value) {
            is ApiResponse.Loading -> {
                Log.d("UserRatingScreen", "Target user loading")
                CircularProgressIndicator()
            }
            is ApiResponse.Success -> {
                Text(userResponse.data.firstName + " " + userResponse.data.lastName)
                Row {
                    Text("Thumbs Up: ${thumbsUpCount.value}")
                    Text("Thumbs Down: ${thumbsDownCount.value
                    }")
                }
            }
            else -> {
                Text("Error")
            }
        }

        when (val ratingsResponse = ratings.value) {
            is ApiResponse.Loading -> {
                Log.d("UserRatingScreen", "Ratings loading")
                CircularProgressIndicator()
            }
            is ApiResponse.Success -> {
                Log.d(
                    "UserRatingScreen",
                    "currentUser: ${currentUser.value}, currentUserLoading: ${currentUserLoading.value}"
                )
                Log.d("UserRatingScreen", "${currentUserHasRating}")
                if (!currentUserHasRating) {
                    Log.d("UserRatingScreen", "Ratings success")
                    Text(
                        text = "Add Rating",
                        modifier = Modifier.clickable {
                            // Show dialog for adding a rating
                            currentRating = null
                            ratingText = ""
                            showDialog = true
                        }
                    )
                }

                LazyColumn {
                    val ratingList = (ratingsResponse as ApiResponse.Success).data
                    Log.d("UserRatingScreen", "Rating list size: ${ratingList.size}")
                    items(ratingList) { rating ->
                        Log.d("UserRatingScreen", "Rating n?")
                        currentUser.value.let { userResponse ->
                            if (userResponse is ApiResponse.Loading) {
                                Log.d("UserRatingScreen", "why?")
                                CircularProgressIndicator()
                            }
                            if (userResponse is ApiResponse.Success) {
                                Log.d("UserRatingScreen", "enfin")
                                RatingItem(
                                    rating = rating,
                                    reviewer = userResponse.data,
                                    isCurrentUserRating = currentUserHasRating,
                                    onEditRating = {
                                        // Show dialog for editing a rating
                                        currentRating = rating
                                        ratingText = rating.comment
                                        thumbUp = rating.thumbUp
                                        thumbDown = !thumbUp
                                        showDialog = true
                                    },
                                    onRemoveRating = {
                                        viewModel.deleteRating(
                                            rating.userId,
                                            rating.id
                                        )
                                        thumbsUpCount.value -= if (thumbUp) 1 else 0
                                        thumbsDownCount.value -= if (thumbDown) 1 else 0

                                    },
                                )
                            }
                            if (userResponse is ApiResponse.Loading) {
                            }
                        }
                    }
                }
            }
            else -> {
                Log.d("UserRatingScreen", "Ratings failed:")

            }
        }
    }
}



