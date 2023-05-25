package com.studhub.app.presentation.listing.details.components

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.R
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class BiddingControlsTest {

    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    @get:Rule
    val composeTestRule = createComposeRule()

    @JvmField
    var rememberable: MutableState<String>? = null

    @Test
    fun biddingControlsAreDisplayed() {

        // Set the ListingImage composable content
        composeTestRule.setContent {
            val price = remember { mutableStateOf("") }
            BiddingControls(price = price, deadline = Date(), onSubmit = {}, hasBid = false)
        }

        // Assert that the ListingImage is displayed with the correct content description
        composeTestRule.onNodeWithText(str(R.string.bidding_price_label)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.bidding_button)).assertIsDisplayed()
    }

    @Test
    fun biddingControlsPriceTypingWorks() {
        val toType = "454.45"
        composeTestRule.setContent {
            rememberable = remember { mutableStateOf("") }
            BiddingControls(price = rememberable!!, deadline = Date(), onSubmit = {}, hasBid = false)
        }
        composeTestRule.onNodeWithText(str(R.string.bidding_price_label)).performTextInput(toType)
        assertEquals(rememberable!!.value, toType)
    }

    @Test
    fun biddingControlsBidButtonTriggersHandler() {
        var test = false;
        composeTestRule.setContent {
            val price = remember { mutableStateOf("") }
            BiddingControls(price = price, deadline = Date(), onSubmit = {test = true}, hasBid = false)
        }
        composeTestRule.onNodeWithText(str(R.string.bidding_button)).performClick()
        assert(test)
    }
}
