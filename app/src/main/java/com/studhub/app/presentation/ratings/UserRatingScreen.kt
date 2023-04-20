package com.studhub.app.presentation.ratings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import kotlinx.coroutines.flow.first

@Composable
fun UserRatingScreen(viewModel: UserRatingViewModel = hiltViewModel()) {
    val ratings = viewModel.ratings.collectAsState(initial = ApiResponse.Loading)
    val currentUser = viewModel.currentUser.collectAsState(initial = ApiResponse.Loading)
    var showDialog by remember { mutableStateOf(false) }
    var currentRating: Rating? by remember { mutableStateOf(null) }
    var ratingText by remember { mutableStateOf("") }
    var thumbUp by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Column {
                Text("Thumbs Up / Thumbs Down")
                Row {
                    Checkbox(checked = thumbUp, onCheckedChange = { thumbUp = it })
                    Checkbox(checked = !thumbUp, onCheckedChange = { thumbUp = !it })
                }
                TextField(value = ratingText, onValueChange = { ratingText = it })
                Button(
                    onClick = {
                        if (currentRating == null) {
                            viewModel.addRating(
                                currentUser.value.let {
                                    if (it is ApiResponse.Success) {
                                        it.data.id
                                    } else {
                                        return@Button
                                    }
                                },
                                Rating(
                                    reviewerId = currentUser.value.let {
                                        if (it is ApiResponse.Success) {
                                            it.data.id
                                        } else {
                                            return@Button
                                        }
                                    },
                                    thumbUp = thumbUp,
                                    thumbDown = !thumbUp,
                                    comment = ratingText
                                )
                            )
                        } else {
                            viewModel.updateRating(
                                currentUser.value.let {
                                    if (it is ApiResponse.Success) {
                                        it.data.id
                                    } else {
                                        return@Button
                                    }
                                },
                                currentRating!!.id,
                                currentRating!!.copy(
                                    thumbUp = thumbUp,
                                    thumbDown = !thumbUp,
                                    comment = ratingText
                                )
                            )
                        }
                        showDialog = false
                    },
                    content = { Text("Submit") }
                )
            }
        }
    }

    Column {
        when (val userResponse = currentUser.value) {
            is ApiResponse.Loading -> {
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
                // handle
            }
        }

        when (val ratingsResponse = ratings.value) {
            is ApiResponse.Loading -> {
                CircularProgressIndicator()
            }
            is ApiResponse.Success -> {
                val currentUserHasRating = currentUser.value.let { userResponse ->
                    if (userResponse is ApiResponse.Success) {
                        ratingsResponse.data.any { it.reviewerId == userResponse.data.id }
                    } else {
                        false
                    }
                }

                if (!currentUserHasRating) {
                    Text(
                        text = "Add Listing",
                        modifier = Modifier.clickable {
                            // Show dialog for adding a rating
                            currentRating = null
                            ratingText = ""
                            showDialog = true
                        }
                    )
                }

                LazyColumn {
                    items(ratingsResponse.data) { rating ->
                        val userResponse = remember(rating.reviewerId) {
                            mutableStateOf<ApiResponse<User>>(ApiResponse.Loading)
                        }
                        LaunchedEffect(rating.reviewerId) {
                            userResponse.value = viewModel.getUser(rating.reviewerId).first()
                        }
                        when (val reviewer = userResponse.value) {
                        is ApiResponse.Loading -> {
                            CircularProgressIndicator()
                        }
                        is ApiResponse.Success -> {
                            val isCurrentUserRating = currentUser.value.let {
                                if (it is ApiResponse.Success) {
                                    it.data.id == rating.reviewerId
                                } else {
                                    false
                                }
                            }
                            RatingItem(
                                rating = rating,
                                reviewer = reviewer.data,
                                isCurrentUserRating = isCurrentUserRating,
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
                            // handle
                        }
                    }
                    }
                }
            }
            else -> {
                // handle
            }
        }
    }
}



@Composable
fun RatingItem(
    rating: Rating,
    reviewer: User,
    isCurrentUserRating: Boolean,
    onEditRating: (Rating) -> Unit,
    onRemoveRating: () -> Unit,
) {
    Column {
        Text(reviewer.firstName + " " + reviewer.lastName)
        Text(rating.comment)

        if (isCurrentUserRating) {
            Row {
                Text(
                    text = "Edit Rating",
                    modifier = Modifier.clickable {
                        onEditRating(rating)
                    }
                )
                Text(
                    text = "Remove Rating",
                    modifier = Modifier.clickable {
                        onRemoveRating()
                    }
                )
            }
        }
    }
}


@Composable
fun EditRatingText(onClick: () -> Unit) {
    Text(
        text = "Edit Rating",
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun RemoveRatingText(onClick: () -> Unit) {
    Text(
        text = "Remove Rating",
        modifier = Modifier.clickable { onClick() }
    )
}
