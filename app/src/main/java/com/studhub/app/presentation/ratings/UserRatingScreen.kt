package com.studhub.app.presentation.ratings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.user.GetCurrentUser
import com.studhub.app.domain.usecase.user.GetUser
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun UserRatingScreen(viewModel: UserRatingViewModel) {
    val ratings = viewModel.ratings.collectAsState(initial = ApiResponse.Loading)
    val currentUser = viewModel.currentUser.collectAsState(initial = ApiResponse.Loading)

    when (val ratingsResponse = ratings.value) {
        is ApiResponse.Loading -> {
            CircularProgressIndicator()
        }
        is ApiResponse.Success -> {
            LazyColumn {
                items(ratingsResponse.data) { rating ->
                    var userResponse by remember { mutableStateOf<ApiResponse<User>>(ApiResponse.Loading) }
                    LaunchedEffect(rating.userId) {
                        userResponse = viewModel.getUser(rating.userId).first()
                    }
                    when (userResponse) {
                        is ApiResponse.Loading -> {
                            CircularProgressIndicator()
                        }
                        is ApiResponse.Success -> {
                            val isCurrentUserRating = currentUser.value.let {
                                if (it is ApiResponse.Success) {
                                    it.data.id == rating.userId
                                } else {
                                    false
                                }
                            }
                            RatingItem(
                                user = (userResponse as ApiResponse.Success<User>).data,
                                isCurrentUserRating = isCurrentUserRating,
                                    onEditRating = { viewModel.updateRating(rating.userId, rating.id, rating) },
                                onRemoveRating = { viewModel.deleteRating(rating.userId, rating.id) },
                            )
                        }
                        else -> {
                            // Handle other states: Failure
                        }
                    }
                }
            }
        }
        else -> {
            // Handle other states: Failure
        }
    }
}



@Composable
fun RatingItem(
    user: User,
    isCurrentUserRating: Boolean,
    onEditRating: () -> Unit,
    onRemoveRating: () -> Unit
) {
    Column {
        Text("${user.firstName} ${user.lastName}")
        if (isCurrentUserRating) {
            Row {
                Text(
                    text = "Edit Rating",
                    modifier = Modifier.clickable {
                        onEditRating()
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
