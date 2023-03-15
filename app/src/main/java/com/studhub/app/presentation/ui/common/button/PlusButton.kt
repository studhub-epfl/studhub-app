package com.studhub.app.presentation.ui.common.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PlusButton(onClick: () -> Unit) {
    FilledIconButton(onClick = onClick) {
        Icon(Icons.Outlined.Add, contentDescription = "Plus Icon")
    }
}

@Preview(showBackground = true)
@Composable
fun PlusButtonPreview() {
    PlusButton(onClick = { })
}
