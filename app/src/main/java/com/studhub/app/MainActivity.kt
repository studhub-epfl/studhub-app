package com.studhub.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.studhub.app.presentation.auth.AuthViewModel
import com.studhub.app.ui.theme.StudHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavHostController
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController()
            StudHubTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    // Going to change, customize colors later.
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(navController = navController)
                }
                var isConnected = rememberSaveable { mutableStateOf(false) }
                checkAuthState(isConnected)
            }
        }
    }

    private fun checkAuthState(isConnected: MutableState<Boolean>) {
        if (!isConnected.value) {
            if (viewModel.isUserAuthenticated) {
                isConnected.value = true
                handleLoginComplete()
            }
        } else {
            navController.navigate("Home")
        }
    }

    private fun handleLoginComplete() = navController.navigate("Home")
}


