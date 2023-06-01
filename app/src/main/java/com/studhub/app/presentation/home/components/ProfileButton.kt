package com.studhub.app.presentation.home.components


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.studhub.app.R

@Composable
fun ProfileButton(onClick: () -> Unit, modifier: Modifier = Modifier) {


    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp), // Use a rounded rectangle shape with custom corner radius
        modifier = modifier.padding(end = 8.dp), // Add horizontal spacing between buttons
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        Text(text = stringResource(R.string.home_button_profile),style = MaterialTheme.typography.titleMedium)
    }
}
