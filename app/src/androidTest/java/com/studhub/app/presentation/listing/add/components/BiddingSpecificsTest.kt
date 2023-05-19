package com.studhub.app.presentation.listing.add.components

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.presentation.ui.theme.StudHubTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.studhub.app.R

@RunWith(AndroidJUnit4::class)
class BiddingSpecificsTest {

    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    @get:Rule
    val composeTestRule = createComposeRule()

    @JvmField
    var checked: MutableState<Boolean>? = null

    @OptIn(ExperimentalMaterial3Api::class)
    @JvmField
    var date: DatePickerState? = null

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun biddingSpecificsElementsAreDisplayed() {

        composeTestRule.setContent {
            checked = remember { mutableStateOf(true) }
            date = rememberDatePickerState()
            BiddingSpecifics(checked = checked!!, date = date!!)
        }

        composeTestRule.onNodeWithContentDescription("Auction").assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.auction_deadline_select)).assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun biddingSpecificsSwitchSuccessfulyChangeState() {

        composeTestRule.setContent {
            checked = remember { mutableStateOf(false) }
            date = rememberDatePickerState()
            BiddingSpecifics(checked = checked!!, date = date!!)
        }

        composeTestRule.onNodeWithContentDescription("Auction").performClick()
        assert(checked!!.value)
    }

}

