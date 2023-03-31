package com.studhub.app.presentation.listing.details.components

import androidx.appcompat.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage


@Composable
fun ListingImage(
    painter: Painter = painterResource(id = R.drawable.abc_btn_radio_to_on_mtrl_000),
    contentDescription: String
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun ListingImagePreview() {
    ListingImage(contentDescription = "test")
}
