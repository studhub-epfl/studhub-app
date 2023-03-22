package com.studhub.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.wrapper.NavigationActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/***
 *
 * this test class is different than HomeScreenTest in the sense
 * that it tests the Navigation from HomeScreen to other screens,
 * not the functionality of HomeScreen
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<NavigationActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun str(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun clickAddListing_navigatesToAddListingScreen() {
        composeTestRule.onNodeWithText(str(R.string.home_button_add_listing)).assertExists()
            .performClick()

        composeTestRule.onNodeWithText(str(R.string.listings_add_title)).assertIsDisplayed()
    }

    @Test
    fun clickBrowse_navigatesToBrowseScreen() {
        composeTestRule.onNodeWithText(str(R.string.home_button_browse)).assertExists()
            .performClick()

        composeTestRule.onNodeWithText(str(R.string.listings_browsing_title)).assertIsDisplayed()
    }

    @Test
    fun clickCart_navigatesToCartScreen() {
        composeTestRule.onNodeWithText(str(R.string.home_button_cart)).assertExists().performClick()
        composeTestRule.onNodeWithText(str(R.string.cart_title)).assertIsDisplayed()
    }

    @Test
    fun clickCart_navigatesToAboutScreen() {
        composeTestRule
            .onNodeWithText(str(R.string.home_button_about)).assertExists()
            .performClick()

        composeTestRule.onNodeWithText(str(R.string.about_title)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.about_content)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.about_contact_title)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.about_contact_content)).assertIsDisplayed()
    }

}



