package com.studhub.app.presentation.listing.details.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun BlockButton(
    color: Color = Color.Black,
    isBlocked: Boolean,
    onBlockClicked: () -> Unit
) {
    IconToggleButton(
        checked = isBlocked,
        onCheckedChange = {
            onBlockClicked()
        }
    ) {
        Icon(
            tint = color,
            modifier = Modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isBlocked) Icons.Filled.Lock else Icons.Outlined.Lock,
            contentDescription = if (isBlocked) "Unblock User" else "Block User"
        )
    }
}
