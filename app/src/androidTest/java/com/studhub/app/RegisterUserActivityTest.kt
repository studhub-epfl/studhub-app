package com.studhub.app

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterUserActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<RegisterUserActivity>()

    @Test
    fun allFormComponentsExist() {
        composeTestRule.activity.setContent { UserForm() }
        composeTestRule.onNodeWithText("Firstname").assertExists()
        composeTestRule.onNodeWithText("Lastname").assertExists()
        composeTestRule.onNodeWithText("Username").assertExists()
        composeTestRule.onNodeWithText("Email").assertExists()
        composeTestRule.onNodeWithText("Phone number").assertExists()
        composeTestRule.onNodeWithText("Add profile picture").assertExists()
        composeTestRule.onNodeWithText("Submit").assertExists()
    }

}
