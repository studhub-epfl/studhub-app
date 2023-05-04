package com.studhub.app.presentation.ui.common.container

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Card(
    modifier: Modifier,
    containerColor: Color? = null,
    contentColor: Color? = null,
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    shape: Shape = CardDefaults.shape,
    content: @Composable ColumnScope.() -> Unit,
) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .padding(8.dp)
            .then(modifier),
        colors = CardDefaults.cardColors(
            containerColor = containerColor ?: MaterialTheme.colorScheme.secondaryContainer,
            contentColor = contentColor ?: MaterialTheme.colorScheme.onSecondaryContainer
        ),
        shape = shape,
        elevation = elevation,
        border = border,
        onClick =  onClick,
        enabled = enabled,
        content = content
    )
}
