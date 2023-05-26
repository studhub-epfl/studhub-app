package com.studhub.app.wrapper

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.studhub.app.presentation.profile.ProfileBlockedScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileBlockedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProfileBlockedScreen()
        }
    }
}
