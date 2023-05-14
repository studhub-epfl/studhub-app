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

        setContent {
//            UserRatingScreen("-NQSUz5EY7-EfBG7PCIu")
            UserRatingScreen(targetUserId = "")
        }
    }

}
