package com.studhub.app.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.R
import com.studhub.app.wrapper.AboutActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AboutScreenTest {

    private fun str(id: Int) = composeTestRule.activity.getString(id)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<AboutActivity>()

    @Test
    fun aboutScreenTest() {
        composeTestRule.onNodeWithText(str(R.string.about_title)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.about_content)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.about_contact_title)).assertIsDisplayed()
        composeTestRule.onNodeWithText(str(R.string.about_contact_content)).assertIsDisplayed()
    }

}
