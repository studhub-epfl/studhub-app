package com.studhub.app.presentation.ui.browse


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studhub.app.domain.model.Listing
import com.studhub.app.presentation.ui.browse.components.CategoryAndSellerInfo
import com.studhub.app.presentation.ui.browse.components.PriceChip
import com.studhub.app.presentation.ui.browse.components.ThumbnailImage

@Composable
fun ListingContent(listing: Listing, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
    ) {
        ThumbnailImage()
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            // Item title
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = listing.name,
                style = MaterialTheme.typography.labelLarge
            )
            // Price chip
            PriceChip(
                price = listing.price,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 5.dp, bottom = 4.dp)
            )
            // Category name and seller
            CategoryAndSellerInfo(
                category = listing.categories[0],
                seller = listing.seller,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 5.dp, bottom = 4.dp)
            )
        }
    }
}
