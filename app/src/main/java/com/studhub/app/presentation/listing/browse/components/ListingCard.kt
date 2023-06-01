package com.studhub.app.presentation.listing.browse.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.studhub.app.R
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.ListingType
import com.studhub.app.presentation.ui.common.container.Card


@Composable
fun ListingCard(listing: Listing, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = listing.pictures.firstOrNull() ?: "",
                contentDescription = listing.name,
                modifier = Modifier.fillMaxWidth(0.35F),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = listing.name,
                    style = MaterialTheme.typography.titleMedium,
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
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    PriceChip(
                        price = listing.price,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    if (listing.categories.isNotEmpty())
                        Text(
                            text = listing.categories[0].name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                }
            }
        }
    }
}
