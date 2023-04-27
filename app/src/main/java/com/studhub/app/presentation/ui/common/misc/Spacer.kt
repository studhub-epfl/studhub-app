package com.studhub.app.presentation.ui.common.misc

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Spacer(size: String = "medium") {

    val height = when (size) {
        "small" -> 8.dp
        "large" -> 24.dp
        else -> 16.dp
    }

    Spacer(modifier = Modifier.height(height))
}
