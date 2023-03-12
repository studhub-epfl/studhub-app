package com.studhub.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.studhub.app.ui.AboutScreen
import com.studhub.app.ui.BrowseScreen
import com.studhub.app.ui.CartScreen
import com.studhub.app.ui.HomeScreen
import com.studhub.app.ui.theme.StudHubTheme

class MainActivity : AppCompatActivity() {
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
                    MainActivityContent()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainActivityContent(navController: NavHostController = rememberNavController()) {
        NavHost(navController = navController, startDestination = "Home") {
            composable("Home") {
                HomeScreen()
            }
            composable("Browse") {
                BrowseScreen()
            }
            composable("Cart") {
                CartScreen()
            }
            composable("Cart") {
                AboutScreen()
            }
            composable("Map") {
                MapsActivity()
            }
        }
    }
}
