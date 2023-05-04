package com.studhub.app.presentation.listing.details.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.presentation.listing.details.DetailedListingViewModel

@Composable
fun FavoriteButton(
    color: Color = Color(0xffE91E63),
    isFavorite: Boolean, onFavoriteClicked: (Boolean) -> Unit, getFavorites: () -> Unit
) {
    LaunchedEffect(Unit) {
        getFavorites()
    }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            onFavoriteClicked(!isFavorite)
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
