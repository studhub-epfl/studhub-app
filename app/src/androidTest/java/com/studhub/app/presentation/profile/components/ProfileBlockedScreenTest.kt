package com.studhub.app.presentation.profile.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.R
import com.studhub.app.wrapper.EditProfileActivity
import com.studhub.app.wrapper.ProfileBlockedActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ProfileBlockedScreenTest {

    private fun str(id: Int) = composeTestRule.activity.getString(id)

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ProfileBlockedActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun profileBlockedTitleTest() {
        // Check if related text is displayed
        composeTestRule.onNodeWithText(str(R.string.profile_blocked_title)).assertIsDisplayed()
    }

    @Test
    fun profileNoBlockedTest() {
        composeTestRule
            .onNodeWithText(str(R.string.profile_blocked_no_blocked))
            .assertIsDisplayed()
    }

    @Test
    fun profileButtonBlockedTest() {
        //composeTestRule.onNodeWithText("").assertIsDisplayed().performClick()
    }
}
