package com.studhub.app.presentation.listing.browse

import androidx.appcompat.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.studhub.app.domain.model.Listing
import com.studhub.app.presentation.ui.theme.StudHubTheme

@Composable
fun ListingScreen(
    listing: Listing, onContactSellerClick: () -> Unit, onFavouriteClick: () -> Unit
) {
    val typography = MaterialTheme.typography
    StudHubTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
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
                    OutlinedButton(
                        onClick = { onFavouriteClick() },
                    ) {
                        Text(text = "Favourite")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = listing.name,
                    style = typography.titleLarge,
                    color = contentColorFor(MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                // Add the placeholder image here
                Image(
                    painter = painterResource(id = R.drawable.abc_btn_radio_to_on_mtrl_000),
                    contentDescription = "Listing Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Description:",
                    style = typography.titleMedium,
                    color = contentColorFor(MaterialTheme.colorScheme.surface)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = listing.description,
                    style = typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    color = contentColorFor(MaterialTheme.colorScheme.surface)
                )
                Spacer(modifier = Modifier.height(35.dp))
                Text(
                    text = "Price:",
                    style = typography.titleMedium,
                    color = contentColorFor(MaterialTheme.colorScheme.surface)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    // $ + price
                    text = "${listing.price} CHF",
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    color = contentColorFor(MaterialTheme.colorScheme.surface)
                )
            }
        }
    }
}
