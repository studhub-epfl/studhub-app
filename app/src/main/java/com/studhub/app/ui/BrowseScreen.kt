package com.studhub.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studhub.app.BrowseList
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.ui.theme.StudHubTheme

@Composable
fun BrowseScreen() {
    StudHubTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
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
