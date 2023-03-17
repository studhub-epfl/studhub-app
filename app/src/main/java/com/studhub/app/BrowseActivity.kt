package com.studhub.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.repository.ListingRepositoryImpl
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.listing.GetListings
import com.studhub.app.presentation.ui.browse.ListingThumbnail
import com.studhub.app.ui.theme.StudHubTheme
import kotlinx.coroutines.runBlocking

class BrowseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            /* TODO - unable to make that work for sprint1, implement it later
            LaunchedEffect(listings) {
                listings = getAllListings()
            }
            */
            val listings = listOf(
                Listing(
                    name = "Algebra for the dummies",
                    seller = User(firstName = "Jacky", lastName = "Chan"),
                    categories = listOf(Category(name = "Books")),
                    description = "Really great book to learn Algebra for entry level readers.",
                    price = 34.50F
                ),
                Listing(
                    name = "Brand new Nike Air One",
                    seller = User(firstName = "Kristina", lastName = "Gordova"),
                    categories = listOf(Category(name = "Clothing")),
                    description = "Branx new shoes, full white and ready for any custom work if needed.",
                    price = 194.25F
                ),
                Listing(
                    name = "Super VTT 2000 with custom paint",
                    seller = User(firstName = "Marc", lastName = "Marquez"),
                    categories = listOf(Category(name = "Mobility")),
                    description = "Robust bike for downhill riding and will " +
                            "look absolutely unique with this custom paint work",
                    price = 1500F
                )
            )
            BrowseList(listings)
        }
    }
}

private var listings = emptyList<Listing>()

private suspend fun getAllListings(): List<Listing> {
    val listingRepository = ListingRepositoryImpl()
    val getListings = GetListings(listingRepository)
    var listings: List<Listing> = emptyList()

    getListings().collect {
        when (it) {
            is ApiResponse.Success -> {
                listings = it.data
            }
            is ApiResponse.Failure -> {/* TODO - show error status on screen */
            }
            is ApiResponse.Loading -> {}
        }
    }

    return listings
}

@Composable
fun BrowseList(listings: List<Listing>) {
    val context = LocalContext.current
    StudHubTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            ) {
                items(listings) { listing ->
                    Spacer(modifier = Modifier.height(6.dp))
                    ListingThumbnail(
                        listing = listing,
                        onClick = {
                            val intent = Intent(context, DetailedListingView::class.java)
                            intent.putExtra("listingTitle", listing.name)
                            intent.putExtra("description", listing.description)
                            intent.putExtra("category", listing.categories[0].name)
                            intent.putExtra("userName", listing.seller.userName)
                            intent.putExtra("firstName", listing.seller.firstName)
                            intent.putExtra("lastName", listing.seller.lastName)
                            intent.putExtra("price", listing.price)
                            context.startActivity(intent)
                        }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Divider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BrowseActivityPreview() {
    val listings = listOf(
        Listing(
            name = "Algebra for the dummies",
            seller = User(firstName = "Jacky", lastName = "Chan"),
            categories = listOf(Category(name = "Books")),
            description = "Really great book to learn Algebra for entry level readers.",
            price = 34.50F
        ),
        Listing(
            name = "Brand new Nike Air One",
            seller = User(firstName = "Kristina", lastName = "Gordova"),
            categories = listOf(Category(name = "Clothing")),
            description = "Branx new shoes, full white and ready for any custom work if needed.",
            price = 194.25F
        ),
        Listing(
            name = "Super VTT 2000 with custom paint",
            seller = User(firstName = "Marc", lastName = "Marquez"),
            categories = listOf(Category(name = "Mobility")),
            description = "Robust bike for downhill riding and will " +
                    "look absolutely unique with this custom paint work",
            price = 1500F
        )
    )
    StudHubTheme {
        BrowseList(listings = listings)
    }
}
