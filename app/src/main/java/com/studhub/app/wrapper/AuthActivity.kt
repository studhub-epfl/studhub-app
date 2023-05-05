package com.studhub.app.wrapper

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import com.studhub.app.AppNavigation
import com.studhub.app.core.Globals
import com.studhub.app.presentation.auth.AuthScreen
import com.studhub.app.presentation.nav.NavBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Scaffold(
                bottomBar = {
                    if (Globals.showBottomBar) {
                        NavBar()
                    }
                },
                content = {
                    AppNavigation()
                })
            AuthScreen(
                navigateToSignUpScreen = {},
                navigateToForgotPasswordScreen = {})
        }
    }
}
