package com.studhub.app.presentation.user.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.listing.browse.components.ThumbnailImage
import com.studhub.app.presentation.listing.details.components.BlockButton

@Composable
fun UserCard(
    user: User, onBlockClicked: () -> Unit,
    isBlocked: Boolean,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
        ) {
            ThumbnailImage()
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                // Item title
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = user.userName,
                    style = MaterialTheme.typography.labelLarge
                )
                Column(modifier = Modifier.align(Alignment.BottomStart)) {
                    BlockButton(onBlockClicked = onBlockClicked, isBlocked = isBlocked)
                }
            }
        }
    }
}
