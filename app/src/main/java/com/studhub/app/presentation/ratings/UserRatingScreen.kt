package com.studhub.app.presentation.ratings

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow


@Composable
fun UserRatingScreen(targetUserId: String, viewModel: IUserRatingViewModel = hiltViewModel<UserRatingViewModel>()) {
    val ratings = viewModel.ratings.collectAsState(initial = ApiResponse.Loading)
    val currentUserLoading = viewModel.currentUserLoading.collectAsState(initial = true)
    val currentUser = viewModel.currentUser.collectAsState(initial = ApiResponse.Loading)
    var showDialog by remember { mutableStateOf(false) }
    var currentRating: Rating? by remember { mutableStateOf(null) }
    var ratingText by remember { mutableStateOf("") }
    var thumbUp by remember { mutableStateOf(false) }
    val targetUser = viewModel.targetUser.collectAsState(initial = ApiResponse.Loading)
    val buttonEnabled = remember { mutableStateOf(false) }

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
                currentUserHasRating = (ratings.value as ApiResponse.Success).data.any { it.reviewerId == currentUserData.data.id }
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
                                    viewModel.addRating(
                                        targetUserId,
                                        Rating(
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
                                }
                                showDialog = false
                                Log.d("UserRatingScreen","False reached $showDialog")
                                viewModel.getUserRatings(targetUserId)
                                Log.d("UserRatingScreen","False Got ratings $showDialog")
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
                    Text("Thumbs Up: ${userResponse.data.thumbsUpCount}")
                    Text("Thumbs Down: ${userResponse.data.thumbsDownCount}")
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
                Log.d("UserRatingScreen", "currentUser: ${currentUser.value}, currentUserLoading: ${currentUserLoading.value}")
                Log.d("UserRatingScreen","${currentUserHasRating}")
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
                    items(ratingsResponse.data, key = { it.id }) { rating ->
                        Log.d("UserRatingScreen", "Rating n?")
                        val userResponse = remember {
                            mutableStateOf<ApiResponse<User>>(ApiResponse.Loading)
                        }
                        LaunchedEffect(rating.reviewerId) {
                            userResponse.value = viewModel.getUserById(rating.reviewerId)
                        }
                        when (val reviewer = userResponse.value) {
                            is ApiResponse.Loading -> {
                                Log.d("UserRatingScreen", "why?")
                                CircularProgressIndicator()
                            }
                            is ApiResponse.Success -> {
                                Log.d("UserRatingScreen", "enfin")
                                RatingItem(
                                    rating = rating,
                                    reviewer = reviewer.data,
                                    isCurrentUserRating = currentUserHasRating,
                                    onEditRating = {
                                        // Show dialog for editing a rating
                                        currentRating = rating
                                        ratingText = rating.comment
                                        thumbUp = rating.thumbUp
                                        showDialog = true
                                    },
                                    onRemoveRating = {
                                        viewModel.deleteRating(
                                            rating.userId,
                                            rating.id
                                        )
                                    },
                                )
                            }
                            else -> {
                                Log.d("UserRatingScreen", "else")
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



