package com.studhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.repository.ListingRepositoryImpl
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.listing.GetListings
import com.studhub.app.ui.theme.StudHubTheme

class BrowseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var listings = emptyList<Listing>()
            LaunchedEffect(listings) {
                listings = getAllListings()
            }
            BrowseList(listings)
        }
    }
}

private suspend fun getAllListings(): List<Listing> {
    val listingRepository = ListingRepositoryImpl()
    val getListings = GetListings(listingRepository)
    lateinit var listings: List<Listing>
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
    StudHubTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            ) {
                items(listings) { listing ->
                    Text(listing.name)
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
            price = 34.50F
        ),
        Listing(
            name = "Brand new Nike Air One",
            seller = User(firstName = "Kristina", lastName = "Gordova"),
            categories = listOf(Category(name = "Clothing")),
            price = 194.25F
        ),
        Listing(
            name = "Super VTT 2000 with custom paint",
            seller = User(firstName = "Marc", lastName = "Marquez"),
            categories = listOf(Category(name = "Mobility")),
            price = 1500F
        )
    )
    StudHubTheme {
        BrowseList(listings = listings)
    }
}
