package com.studhub.app.presentation.listing.add.components

import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.listing.browse.components.CategoryAndSellerInfo
import com.studhub.app.presentation.listing.browse.components.PriceChip
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryAndSellerInfoTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun categoryAndSellerInfoAreDisplayed() {
        val cat = "test"
        composeTestRule.setContent {
            CategoryAndSellerInfo(category = Category(name = cat), seller = User())
        }

        composeTestRule.onNodeWithText("Category: $cat").assertIsDisplayed()
    }

    @Test
    fun categoryAndSellerInfoTakesCorrectModifier() {
        composeTestRule.setContent {
            CategoryAndSellerInfo(
                category = Category(name = ""),
                seller = User(),
            modifier = Modifier.width(300.dp))
        }

        composeTestRule.onNodeWithTag("categoryAndSellerInfoBox").assertWidthIsEqualTo(300.dp)
    }
}

