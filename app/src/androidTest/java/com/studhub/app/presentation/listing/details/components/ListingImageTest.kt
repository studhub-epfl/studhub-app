package com.studhub.app.presentation.listing.details.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListingImageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun listingImage_isDisplayed() {
        // The content description for the test
        val testContentDescription = "Test Content Description"

        // Set the ListingImage composable content
        composeTestRule.setContent {
            ListingImage(contentDescription = testContentDescription)
        }

        // Assert that the ListingImage is displayed with the correct content description
        composeTestRule.onNodeWithContentDescription(testContentDescription).assertExists()
    }

    @Test
    fun listingImage_fillsMaxWidth() {
        // The content description for the test
        val testContentDescription = "Test Content Description"

        // Set the ListingImage composable content within a parent box with fixed size
        composeTestRule.setContent {
            Box(modifier = Modifier.size(300.dp)) {
                ListingImage(contentDescription = testContentDescription)
            }
        }

        // Assert that the ListingImage fills the width of the parent
        composeTestRule.onNodeWithContentDescription(testContentDescription)
            .assertWidthIsEqualTo(300.dp)
    }

    @Test
    fun listingImage_hasCorrectHeight() {
        // The content description for the test
        val testContentDescription = "Test Content Description"

        // Set the ListingImage composable content
        composeTestRule.setContent {
            ListingImage(contentDescription = testContentDescription)
        }

        // Assert that the ListingImage has correct height
        composeTestRule.onNodeWithContentDescription(testContentDescription)
            .assertHeightIsEqualTo(200.dp)
    }
}
