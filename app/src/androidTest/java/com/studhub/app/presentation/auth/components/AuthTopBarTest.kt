package com.studhub.app.presentation.auth.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthTopBarTest {
    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun authTopBarDisplaysCorrectTitle() {
        composeTestRule.setContent {
            AuthTopBar()
        }

        composeTestRule.onNodeWithText(str(R.string.auth_title)).assertIsDisplayed()
    }
}
