package com.studhub.app.presentation.ui.browse

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.ui.theme.StudHubTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingThumbnailScreen(
    viewModel: ListingThumbnailViewModel,
    onClick: () -> Unit,
    navController: NavController,
) {    Card(
    onClick = onClick,
    modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
){
    StudHubTheme {
        ListingContent(listing = viewModel.listing, onClick = onClick)
    }
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
    lateinit var navController: NavHostController
    navController = rememberNavController()
    val viewModel = ListingThumbnailViewModel(listing)
    ListingThumbnailScreen(viewModel = viewModel, onClick = {}, navController = navController)
}
