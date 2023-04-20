package com.studhub.app.wrapper

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.studhub.app.presentation.ratings.UserRatingScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatingActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val targetUserId = intent.getStringExtra(TARGET_USER_ID) ?: throw IllegalArgumentException("Missing target user ID")

        setContent {
            UserRatingScreen(targetUserId)
        }
    }

    companion object {
        const val TARGET_USER_ID = "targetUserId"
    }
}
