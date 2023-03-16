package com.studhub.app.presentation.ui.browse

import android.graphics.Paint.Align
import android.view.RoundedCorner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import kotlin.text.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingThumbnail(listing: Listing) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        ThumbnailImage()
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            //Item title
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = listing.name,
                style = MaterialTheme.typography.labelLarge
            )
            //price chip
            Box(
                modifier = Modifier
                    .border(
                        BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        RoundedCornerShape(topEnd = 4.dp)
                    )
                    .background(
                        MaterialTheme.colorScheme.background,
                        RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                    )
                    .align(Alignment.BottomStart)
                    .padding(all = 4.dp)
            ) {
                Text(text = listing.price.toString() + " chf")
            }
            //category name and seller
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                Column {
                    Text(
                        text = "Category: " + listing.categories[0].name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Sold by: " + listing.seller.firstName + " " + listing.seller.lastName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

@Composable
private fun ThumbnailImage() {
    Box(
        modifier = Modifier
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary))
    ) {
        Image(
            imageVector = Icons.Filled.AccountBox,
            modifier = Modifier.aspectRatio(1f),
            contentDescription = "placeholder"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListingThumbnailPreview() {
    val listing = Listing(
        name = "Scooter brand new from 2021",
        seller = User(firstName = "Jimmy", lastName = "Poppin"),
        categories = listOf(Category(name = "Mobility")),
        price = 1560.45F
    )
    ListingThumbnail(listing = listing)
}
