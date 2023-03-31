package com.studhub.app.presentation.listing.details.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage

@Composable
fun ListingDescription(description: String) {
    Text(
        text = "Description:",
        style = MaterialTheme.typography.titleMedium,
        color = contentColorFor(MaterialTheme.colorScheme.surface)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = contentColorFor(MaterialTheme.colorScheme.surface)
    )
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun ListingDescriptionPreview() {
    ListingDescription(description = LoremIpsum(25).toString())
}
