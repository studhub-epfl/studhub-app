package com.studhub.app.presentation.listing.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.studhub.app.R
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.ListingType
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.container.Carousel
import com.studhub.app.presentation.ui.common.misc.Spacer
import com.studhub.app.presentation.ui.common.text.BigLabel

/**
 * Main composable for the detailed listing screen. It contains nearly all the elements to be displayed.
 *
 * @param onMeetingPointClick handler for when the user clicks the meeting point button
 * @param listing the listing we're displaying details about
 * @param onContactSellerClick handler for when we click on the contact seller button
 * @param isFavorite whether or not the listing is a favourite of the viewing user
 * @param isBlocked whether or not the viewing user blocked the seller of this listing
 * @param onFavoriteClicked handler for when the user clicks on the favourite button
 * @param onBlockedClicked handler for when the user clicks on the block button
 * @param onRateUserClick handler for when the user clicks on the Rate button
 * @param onBidPlaced handler for when the user clicks on the bid button
 * @param hasBid whether or not the user detains the current highest bidding on the listing
 */
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
            .padding(8.dp)
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
                    label = stringResource(R.string.listing_details_contact_seller_label)
                )
            }

            Box(modifier = Modifier.testTag("RateUserButton")) {
                BasicFilledButton(
                    onClick = { onRateUserClick() },
                    label = stringResource(R.string.listing_details_rate_user_label)
                )
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

        Carousel(modifier = Modifier.fillMaxWidth(), pictures = listing.pictures)

        Spacer("large")

        BigLabel(label = listing.name)

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
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                BasicFilledButton(
                    label = stringResource(R.string.listing_details_meeting_point_button),
                    onClick = onMeetingPointClick,
                )
            }
        }

        Spacer("large")
        Spacer("large")
        Spacer("large")
        Spacer("large")
        Spacer("large")
        Spacer("large")
    }
}
