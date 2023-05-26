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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.R
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import java.text.DecimalFormat

@Composable
fun ListingPrice(price: Float) {
    Text(
        text = stringResource(R.string.listing_details_price),
        style = MaterialTheme.typography.titleMedium,
        color = contentColorFor(MaterialTheme.colorScheme.surface)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "${DecimalFormat("#,##0.00").format(price)} CHF",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .testTag("price"),
        color = contentColorFor(MaterialTheme.colorScheme.surface)
    )
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun ListingPricePreview() {
    ListingPrice(123.48576f)
}
