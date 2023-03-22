package com.studhub.app.presentation.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.wrapper.HomeActivity
import com.studhub.app.R
import com.studhub.app.data.repository.MockAuthRepositoryImpl
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeTest {

    private fun str(id: Int) = composeTestRule.activity.getString(id)

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HomeActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun homeScreen_displayedElements() {
        composeTestRule.onNodeWithText(str(R.string.home_title)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.home_featured_items_title)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.home_button_add_listing)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.home_button_browse)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.home_button_cart)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.home_button_about)).assertIsDisplayed()
    }

    @Test
    fun homeScreen_welcomeText_displaysUserName() {
        val userName = MockAuthRepositoryImpl.loggedInUser.userName
        val format = str(R.string.home_welcome_name_message)
        val message = String.format(format, userName)

        composeTestRule.apply {
            onNodeWithText(message).assertIsDisplayed()
        }
    }
}
