package com.studhub.app.presentation.listing.browse.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.studhub.app.R
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.ListingType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingCard(listing: Listing, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
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
                Column(modifier = Modifier.align(Alignment.BottomStart)) {
                    if (listing.type == ListingType.BIDDING) {
                        Text(
                            modifier = Modifier.padding(3.dp),
                            text = stringResource(R.string.bidding_type_indicator),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    PriceChip(
                        price = listing.price
                    )
                }
                // Category name and seller
                CategoryAndSellerInfo(
                    category = if (listing.categories.isEmpty()) Category() else listing.categories[0],
                    seller = listing.seller,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 5.dp, bottom = 4.dp)
                )
            }
        }
    }

}
