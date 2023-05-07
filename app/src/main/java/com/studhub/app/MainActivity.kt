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
import com.studhub.app.data.network.NetworkStatus
import com.studhub.app.presentation.auth.AuthViewModel
import com.studhub.app.presentation.nav.NavBar
import com.studhub.app.presentation.ui.theme.StudHubTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavHostController

    private val viewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var networkManager: NetworkStatus

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
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
        val isUserSignedOut = !viewModel.getAuthState().collectAsState().value
        if (isUserSignedOut) {
            Globals.showBottomBar = false
            navController.navigate("Auth")
        } else {
            if (viewModel.isEmailVerified) {
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
        val networkStatus = networkManager.connectivityStatus().collectAsState(initial = true).value

        if (!networkStatus) {
            Utils.displayMessage(applicationContext, stringResource(R.string.error_network_off))
        }
    }

}
