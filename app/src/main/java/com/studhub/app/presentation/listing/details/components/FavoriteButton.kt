package com.studhub.app.presentation.listing.details.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun FavoriteButton(
    color: Color = MaterialTheme.colorScheme.primary,
    isFavorite: Boolean,
    onFavoriteClicked: () -> Unit
) {
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            onFavoriteClicked()
        }
    ) {
        Icon(
            tint = color,
            modifier = Modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
        )
    }
}
