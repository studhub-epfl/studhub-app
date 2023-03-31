package com.studhub.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.wrapper.NavigationActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
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

    private fun str(id: Int) = composeTestRule.activity.getString(id)

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<NavigationActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun clickAddListing_navigatesToAddListingScreen() {
        composeTestRule
            .onNodeWithText(str(R.string.home_button_add_listing)).assertExists()
            .performScrollTo()
            .performClick()

        composeTestRule.onNodeWithText(str(R.string.listings_add_title)).assertIsDisplayed()
    }

    @Test
    fun clickBrowse_navigatesToBrowseScreen() {
        composeTestRule
            .onNodeWithText(str(R.string.home_button_browse)).assertExists()
            .performScrollTo()
            .performClick()

        composeTestRule.onNodeWithText(str(R.string.listings_browsing_title)).assertIsDisplayed()
    }

    @Test
    fun clickCart_navigatesToCartScreen() {
        composeTestRule
            .onNodeWithText(str(R.string.home_button_cart)).assertExists()
            .performScrollTo()
            .performClick()
        composeTestRule.onNodeWithText(str(R.string.cart_title)).assertIsDisplayed()
    }

    @Test
    fun clickProfile_navigateToProfileScreenAndSignOut() {
        composeTestRule
            .onNodeWithText(str(R.string.home_button_profile)).assertExists()
            .performScrollTo()
            .performClick()

        composeTestRule.onNodeWithText(str(R.string.profile_title)).assertIsDisplayed()

        composeTestRule.onNodeWithText(str(R.string.profile_btn_sign_out)).performClick()

        composeTestRule.onNodeWithText(str(R.string.auth_title)).assertIsDisplayed()
    }

    @Test
    fun clickProfile_navigateToProfileScreenAndSignOutAndCheckFavoriteListings() {
        composeTestRule
            .onNodeWithText(str(R.string.home_button_profile)).assertExists()
            .performScrollTo()
            .performClick()

        composeTestRule.onNodeWithText(str(R.string.profile_btn_display_favs))
            .assertExists()
            .performScrollTo()
            .performClick()

        composeTestRule.onNodeWithText(str(R.string.profile_favorites_title))
            .assertIsDisplayed()
    }

    @Test
    fun clickProfile_navigateToProfileThenEditThenSave() {
        composeTestRule
            .onNodeWithText(str(R.string.home_button_profile)).assertExists()
            .performScrollTo()
            .performClick()

        composeTestRule.onNodeWithText(str(R.string.profile_title)).assertIsDisplayed()

        // click the EDIT button on the profile screen
        composeTestRule.onNodeWithText(str(R.string.profile_btn_edit_profile)).performClick()

        // assert edit profile title is displayed i.e. we are on the edit profile page
        composeTestRule.onNodeWithText(str(R.string.profile_edit_title)).assertIsDisplayed()

        // click the save button
        composeTestRule
            .onNodeWithText(str(R.string.profile_edit_form_btn_save))
            .performScrollTo()
            .performClick()

        // assert we are back on the profile page
        composeTestRule.onNodeWithText(str(R.string.profile_title)).assertIsDisplayed()
    }

    @Test
    fun clickCart_navigatesToAboutScreen() {
        composeTestRule
            .onNodeWithText(str(R.string.home_button_about)).assertExists()
            .performScrollTo()
            .performClick()

        composeTestRule.onNodeWithText(str(R.string.about_title)).assertIsDisplayed()
    }
}
