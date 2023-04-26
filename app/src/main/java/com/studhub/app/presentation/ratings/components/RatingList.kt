package com.studhub.app.presentation.ratings.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import java.util.*

@Composable
fun RatingsList(
    ratings: State<ApiResponse<List<Rating>>>,
    currentUser: State<ApiResponse<User>>,
    currentUserHasRating: Boolean,
    onEditRating: (Rating) -> Unit,
    onRemoveRating: (Rating) -> Unit
) {
    when (val ratingsResponse = ratings.value) {
        is ApiResponse.Loading -> {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp, start = 16.dp))
        }
        is ApiResponse.Success -> {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .fillMaxWidth(0.7f)
            ) {
                val ratingList = (ratingsResponse as ApiResponse.Success).data
                items(ratingList) { rating ->
                    currentUser.value.let { userResponse ->
                        if (userResponse is ApiResponse.Loading) {
                            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp, start = 16.dp))
                        }
                        if (userResponse is ApiResponse.Success) {
                            RatingItem(
                                rating = rating,
                                reviewer = userResponse.data,
                                isCurrentUserRating = currentUserHasRating,
                                onEditRating = {
                                    onEditRating(rating)
                                },
                                onRemoveRating = {
                                    onRemoveRating(rating)
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
