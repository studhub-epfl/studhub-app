package com.studhub.app.presentation.nav

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavBarDefaultsTest {

    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            NavBar()
        }
    }

    @Test
    fun navBarButtonsAreDisplayed() {
        composeTestRule.onNodeWithText(str(R.string.nav_home_button)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.nav_browse_button)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.nav_sell_button)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.nav_chat_button)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.nav_profile_button)).assertIsDisplayed()
    }

}
