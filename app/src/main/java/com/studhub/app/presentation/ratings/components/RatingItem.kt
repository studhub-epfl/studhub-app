package com.studhub.app.presentation.ratings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User

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

