package com.studhub.app.presentation.listing.browse.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun PriceChip(
    price: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag("PriceChipBox")
            .wrapContentSize(Alignment.BottomStart)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                RoundedCornerShape(topEnd = 4.dp)
            )
            .background(
                MaterialTheme.colorScheme.background,
                RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
            )
            .padding(all = 4.dp)
    ) {
        Text(
            modifier = Modifier.testTag("PriceChipText"),
            text = String.format("%.2f chf", price),
            color = MaterialTheme.colorScheme.onBackground)
    }
}
