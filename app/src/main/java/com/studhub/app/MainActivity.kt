package com.studhub.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.studhub.app.core.Globals
import com.studhub.app.core.utils.Utils
import com.studhub.app.presentation.auth.AuthViewModel
import com.studhub.app.presentation.nav.NavBar
import com.studhub.app.presentation.ui.theme.StudHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavHostController

    private val authViewModel by viewModels<AuthViewModel>()

    private val viewModel by viewModels<MainViewModel>()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController()
            StudHubTheme {
                Scaffold(
                    bottomBar = {
                        if (Globals.showBottomBar) {
                            NavBar(navController = navController)
                        }
                    },
                    content = {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            // Going to change, customize colors later.
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AppNavigation(navController = navController)
                        }
                        AuthState()
                        ConnectivityState()
                    })
            }
        }
    }

    @Composable
    private fun AuthState() {
        val isUserSignedOut = !authViewModel.getAuthState().collectAsState().value
        if (isUserSignedOut) {
            Globals.showBottomBar = false
            navController.navigate("Auth")
        } else {
            if (authViewModel.isEmailVerified) {
                Globals.showBottomBar = true
                navController.navigate("Home")
            } else {
                Globals.showBottomBar = false
                navController.navigate("Auth/VerifyEmail")
            }
        }
    }

    @Composable
    private fun ConnectivityState() {
        val networkStatus = viewModel.getNetworkState().collectAsState().value

        if (!networkStatus) {
            Utils.displayMessage(applicationContext, stringResource(R.string.error_network_off))
        } else {
            viewModel.flushCachedMessages()
        }
    }

}
