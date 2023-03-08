package com.studhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.studhub.app.ui.theme.StudHubTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudHubTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    // Going to change, customize colors later.
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background

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
            HomePage(
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
fun HomePage(onAddListingClick: () -> Unit, onBrowseClick: () -> Unit) {
    StudHubTheme() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Basic Home Page",
                color = MaterialTheme.colors.secondary,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)

            )
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

    StudHubTheme {
        Text(text = "Add Listing")
        Surface(
            // Going to change, customize colors later.
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.primary

        ) {}
    }
}

@Composable
fun BrowseScreen() {
    StudHubTheme {
        Text(text = "Browse")
        Surface(
            // Going to change, customize colors later.
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.secondaryVariant

        ) {}
    }
}
