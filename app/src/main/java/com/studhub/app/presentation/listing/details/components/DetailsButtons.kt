package com.studhub.app.presentation.listing.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage

@Composable
fun DetailsButtons(onContactSellerClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // "Contact seller" button
        Button(
            onClick = { onContactSellerClick() },
        ) {
            Text(text = "Contact seller")
        }
        // "Favourite" button
        FavoriteButton()
    }
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun DetailsButtonsPreview() {
    DetailsButtons(onContactSellerClick = {})
}
