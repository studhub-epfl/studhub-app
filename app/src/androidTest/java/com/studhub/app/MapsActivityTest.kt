package com.studhub.app

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.google.android.gms.maps.model.LatLng
import org.junit.Rule
import org.junit.Test

class MapsActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MapsActivity>()

    @Test
    fun testMapDisplayed() {
        // Check that the map is displayed
        composeTestRule.onNodeWithTag("googleMap").assertIsDisplayed()
    }

    @Test
    fun testMarkerDisplayed() {
        // Check that the marker is displayed
        composeTestRule.onNodeWithText("Satellite").assertExists()
    }

    @Test
    fun testMarkerClick() {
        // Check that the toast is displayed when the marker is clicked
        val satellite = LatLng(46.520544, 6.567825)
        composeTestRule.onNodeWithTag(satellite.toString()).performClick()
        composeTestRule.onNodeWithText("Marker clicked at $satellite").assertIsDisplayed()
    }


}
