package com.studhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.studhub.app.ui.theme.StudHubBlue
import com.studhub.app.ui.theme.StudHubTheme
import androidx.compose.material.Icon as Icon1

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
                onAddListingClicked = { navController.navigate("AddListing") },
                onBrowseClicked = { navController.navigate("Browse") }
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
fun HomePage(onAddListingClicked: () -> Unit, onBrowseClicked: () -> Unit) {
    val menuExpanded = remember { mutableStateOf(false) }

    StudHubTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ) {
            // Horizontal purple stripe
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.primary)
            )

            // Burger menu button
            IconButton(
                onClick = { menuExpanded.value = !menuExpanded.value },
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon1(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colors.onBackground
                )
            }

            if (menuExpanded.value) {
                // Expanded menu with browse and add listing buttons
                Column(
                    modifier = Modifier
                        .padding(end = 72.dp, bottom = 96.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    IconButton(
                        onClick = {
                            menuExpanded.value = false
                            onBrowseClicked()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(color = MaterialTheme.colors.secondary, shape = CircleShape)
                    ) {
                        Icon1(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Browse",
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    IconButton(
                        onClick = {
                            menuExpanded.value = false
                            onAddListingClicked()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(color = MaterialTheme.colors.secondary, shape = CircleShape)
                    ) {
                        Icon1(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Listing",
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }
                }
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
