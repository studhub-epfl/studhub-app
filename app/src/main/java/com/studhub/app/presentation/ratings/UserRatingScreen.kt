package com.studhub.app.presentation.ratings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
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
                    TextField(
                        value = ratingText,
                        onValueChange = { ratingText = it },
                        modifier = Modifier.weight(1f).fillMaxWidth()
                    )
                    Button(
                        onClick = {
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
                                    viewModel.getUserRatings(targetUserId)
                                } else {
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

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        when (val userResponse = targetUser.value) {
            is ApiResponse.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is ApiResponse.Success -> {
                Text(
                    text = userResponse.data.firstName + " " + userResponse.data.lastName,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Thumbs Up: ${thumbsUpCount.value}", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        "Thumbs Down: ${
                            thumbsDownCount.value
                        }", fontSize = 20.sp
                    )
                }
            }
            else -> {
                Text("Error", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (val ratingsResponse = ratings.value) {
            is ApiResponse.Loading -> {
                CircularProgressIndicator()
            }
            is ApiResponse.Success -> {
//                if (!currentUserHasRating) {
                TextButton(
                    onClick = {
                        currentRating = null
                        ratingText = ""
                        showDialog = true
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.DarkGray)
                ) {
                    Text("Add Rating", fontSize = 14.sp)
                }
//                }

                LazyColumn(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .clip(MaterialTheme.shapes.medium)
                        .fillMaxWidth(0.7f)

                ) {
                    val ratingList = (ratingsResponse as ApiResponse.Success).data
                    items(ratingList) { rating ->
                        currentUser.value.let { userResponse ->
                            if (userResponse is ApiResponse.Loading) {
                                CircularProgressIndicator()
                            }
                            if (userResponse is ApiResponse.Success) {
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

            }
        }
    }
}



