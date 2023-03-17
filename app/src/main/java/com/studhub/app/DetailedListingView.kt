package com.studhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.ui.ListingScreen

class DetailedListingView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val listing = Listing(
                name = intent.getStringExtra("listingTitle")!!,
                description = intent.getStringExtra("description")!!,
                categories = listOf(Category(name = intent.getStringExtra("category")!!)),
                seller = User(
                    userName = intent.getStringExtra("userName")!!,
                    firstName = intent.getStringExtra("firstName")!!,
                    lastName = intent.getStringExtra("lastName")!!,
                ),
                price = intent.getFloatExtra("price", 0F)
            )
            ListingScreen(
                listing = listing,
                onContactSellerClick = { /*TODO*/ },
                onFavouriteClick = { /* TODO */ })
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val listing = Listing(
        name = "Large white wooden desk",
        description = "This is the perfect desk for a home workplace",
        categories = listOf(Category(name = "Furniture")),
        seller = User(
            userName = "SuperChad",
            firstName = "Josh",
            lastName = "Marley",
        ),
        price = 545.45F
    )
    ListingScreen(
        listing = listing,
        onContactSellerClick = { /*TODO*/ },
        onFavouriteClick = { /* TODO */ })
}
