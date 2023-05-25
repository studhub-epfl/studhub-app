package com.studhub.app.presentation.listing.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.ListingType
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.container.Carousel
import com.studhub.app.presentation.ui.common.misc.Spacer
import com.studhub.app.presentation.ui.common.text.BigLabel

@Composable
fun Details(
    onMeetingPointClick: () -> Unit,
    listing: Listing,
    onContactSellerClick: () -> Unit,
    isFavorite: Boolean,
    isBlocked: Boolean,
    onFavoriteClicked: () -> Unit,
    onBlockedClicked: () -> Unit,
    onRateUserClick: () -> Unit,
    onBidPlaced: () -> Unit,
    bid: MutableState<String>,
    hasBid: Boolean
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.testTag("ContactSellerButton")) {
                BasicFilledButton(
                    onClick = { onContactSellerClick() },
                    label = "Contact seller"
                )
            }

            Box(modifier = Modifier.testTag("RateUserButton")) {
                BasicFilledButton(onClick = { onRateUserClick() }, label = "Rate user")
            }

            // "Favorite" button
            Box(modifier = Modifier.testTag("ContactSellerButton")) {
                FavoriteButton(isFavorite = isFavorite, onFavoriteClicked = onFavoriteClicked)
            }

            Box(modifier = Modifier.testTag("BlockedButton")) {
                BlockButton(isBlocked = isBlocked, onBlockClicked = onBlockedClicked)
            }
        }


        Spacer("large")

        BigLabel(label = listing.name)

        Spacer("large")

        Carousel(modifier = Modifier.fillMaxWidth(0.8F), pictures = listing.pictures)

        Spacer("large")

        ListingDescription(description = listing.description)

        Spacer("large")

        ListingPrice(price = listing.price)

        if (listing.type == ListingType.BIDDING) {
            Spacer("large")
            BiddingControls(
                price = bid,
                deadline = listing.biddingDeadline,
                onSubmit = { onBidPlaced() },
                hasBid = hasBid
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        val meetingPoint = listing.meetingPoint
        if (meetingPoint != null) {
            Button(
                onClick = onMeetingPointClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("View Meeting Point")
            }
        }
    }
}
