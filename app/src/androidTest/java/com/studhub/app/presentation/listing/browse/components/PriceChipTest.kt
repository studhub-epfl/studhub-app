package com.studhub.app.presentation.listing.add.components

import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.presentation.listing.browse.components.PriceChip
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PriceChipTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun priceChipIsDisplayed() {
        val price = "454.45"
        composeTestRule.setContent {
            PriceChip(price = price.toFloat())
        }

        composeTestRule.onNodeWithTag("PriceChipBox").assertIsDisplayed()
        composeTestRule.onNodeWithTag("PriceChipText").assertIsDisplayed()
    }

    @Test
    fun priceChipTakesCorrectModifier() {
        val price = "454.45"
        composeTestRule.setContent {
            PriceChip(price = price.toFloat(), modifier = Modifier.width(300.dp))
        }

        composeTestRule.onNodeWithTag("PriceChipBox").assertWidthIsEqualTo(300.dp)
    }
}

