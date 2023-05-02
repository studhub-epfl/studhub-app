package com.studhub.app.presentation.nav

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.MainActivity
import com.studhub.app.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavBarTest {

    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun navBarButtonsAreDisplayed() {
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_home_button)}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_browse_button)}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_sell_button)}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_chat_button)}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_profile_button)}")
            .assertIsDisplayed()
    }

    @Test
    fun navBarNavigateToHomeSelectsIt() {
        val node = composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_home_button)}")
        node.performClick()
        node.assertIsSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_browse_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_sell_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_chat_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_profile_button)}")
            .assertIsNotSelected()
    }

    @Test
    fun navBarNavigateToBrowseSelectsIt() {
        val node = composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_browse_button)}")
        node.performClick()
        node.assertIsSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_home_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_sell_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_chat_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_profile_button)}")
            .assertIsNotSelected()
    }

    @Test
    fun navBarNavigateToCreateListingSelectsIt() {
        val node = composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_sell_button)}")
        node.performClick()
        node.assertIsSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_browse_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_home_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_chat_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_profile_button)}")
            .assertIsNotSelected()
    }

    @Test
    fun navBarNavigateToChatSelectsIt() {
        val node = composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_chat_button)}")
        node.performClick()
        node.assertIsSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_browse_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_sell_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_home_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_profile_button)}")
            .assertIsNotSelected()
    }

    @Test
    fun navBarNavigateToProfileSelectsIt() {
        val node = composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_profile_button)}")
        node.performClick()
        node.assertIsSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_browse_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_sell_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_chat_button)}")
            .assertIsNotSelected()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_home_button)}")
            .assertIsNotSelected()
    }

    @Test
    fun navBarLabelsAreCorrect() {
        val node = composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_profile_button)}")
        node.performClick()
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_home_button)}")
            .assertTextContains(str(R.string.nav_home_button))
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_browse_button)}")
            .assertTextContains(str(R.string.nav_browse_button))
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_sell_button)}")
            .assertTextContains(str(R.string.nav_sell_button))
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_chat_button)}")
            .assertTextContains(str(R.string.nav_chat_button))
        composeTestRule.onNodeWithTag("NavBar${str(R.string.nav_profile_button)}")
            .assertTextContains(str(R.string.nav_profile_button))
    }
}
