package com.studhub.app.wrapper

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.studhub.app.presentation.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HomeScreen(
                onAddListingClick = { /*TODO*/ },
                onBrowseClick = { /*TODO*/ },
                onAboutClick = { /*TODO*/ }) {
                
            }
        }
    }
}