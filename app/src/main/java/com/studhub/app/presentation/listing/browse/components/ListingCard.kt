package com.studhub.app.presentation.listing.browse.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.studhub.app.R
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
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            ThumbnailImage()
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = listing.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    if (listing.type == ListingType.BIDDING) {
                        Text(
                            text = stringResource(R.string.bidding_type_indicator),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                    PriceChip(
                        price = listing.price,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Category: " + (if (listing.categories.isEmpty()) "N/A" else listing.categories[0].name),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Seller: ${listing.seller.firstName} ${listing.seller.lastName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
