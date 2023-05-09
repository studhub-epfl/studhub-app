package com.studhub.app.ratings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.presentation.ratings.UserRatingScreen
import com.studhub.app.wrapper.RatingActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UserRatingScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<RatingActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun userHeaderIsDisplayed() {
        composeTestRule.onNodeWithTag("UserHeader").assertIsDisplayed()
    }

    @Test
    fun addRatingButtonIsDisplayed() {

        composeTestRule.onNodeWithTag("AddRatingButton").assertIsDisplayed()
    }

    @Test
    fun ratingsListIsDisplayed() {
        composeTestRule.onNodeWithTag("RatingsList").assertIsDisplayed()
    }
}
