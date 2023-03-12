package com.studhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.studhub.app.presentation.ui.BigLabel
import com.studhub.app.ui.theme.StudHubTheme

class HomePageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudHubTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    // Going to change, customize colors later.
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {
                    AppNavigation()
                }
            }
        }
    }
}
@Preview
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") {
            HomeScreen(
                onAddListingClick = { navController.navigate("AddListing") },
                onBrowseClick = { navController.navigate("Browse") }
            )
        }
        composable("AddListing") {
            AddListingScreen()
        }
        composable("Browse") {
            BrowseScreen()
        }
    }
}

@Composable
fun HomeScreen(onAddListingClick: () -> Unit, onBrowseClick: () -> Unit) {
    StudHubTheme() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            BigLabel(label = "Home Page")
            Button(
                onClick = onAddListingClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add Listing")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onBrowseClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Browse")
            }
        }
    }
}

@Composable
fun AddListingScreen() {
    Text(text = "listing")
}

@Composable
fun BrowseScreen() {
    Text(text = "browse")
}
