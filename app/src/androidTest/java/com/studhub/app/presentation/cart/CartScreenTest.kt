package com.studhub.app.presentation.cart

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.R
import com.studhub.app.wrapper.CartActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CartScreenTest {
    private fun str(id: Int) = composeTestRule.activity.getString(id)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<CartActivity>()

    @Test
    fun cartScreenTest() {
        // Check if related text is displayed
        composeTestRule.onNodeWithText(str(R.string.cart_title)).assertIsDisplayed()
    }

}
