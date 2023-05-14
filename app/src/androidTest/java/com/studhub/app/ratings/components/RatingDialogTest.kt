package com.studhub.app.ratings.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.ratings.components.RatingDialog
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RatingDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val currentUser = mutableStateOf<ApiResponse<User>>(ApiResponse.Success(User("123", "John", "Doe")))
    private val currentUserLoading = mutableStateOf(false)
    private val showDialog = mutableStateOf(true)
    private var thumbUp = mutableStateOf(true)
    private var ratingText = mutableStateOf("Good job!")

    @Before
    fun setup() {
        composeTestRule.setContent {
            RatingDialog(
                showDialog = showDialog.value,
                setShowDialog = { showDialog.value = false },
                onSubmit = { thumb, text ->
                    thumbUp.value = thumb
                    ratingText.value = text
                },
                thumbUp = thumbUp.value,
                setThumbUp = { thumbUp.value = it },
                ratingText = ratingText.value,
                setRatingText = { ratingText.value = it },
                currentUser = currentUser,
                currentUserLoading = currentUserLoading
            )
        }
    }

    @Test
    fun showsDialog() {
        composeTestRule.onNodeWithText("Submit").assertIsDisplayed()
    }

    @Test
    fun showsCorrectThumbStatus() {
        composeTestRule.onNodeWithTag("ThumbUpCheckbox").assertIsOn()
        composeTestRule.onNodeWithTag("ThumbDownCheckbox").assertIsOff()

    }

    @Test
    fun showsCorrectRatingText() {
        composeTestRule.onNodeWithText(ratingText.value).assertExists()
    }

    @Test
    fun submitButtonIsEnabled() {
        composeTestRule.onNodeWithText("Submit").assertIsEnabled()
    }

    @Test
    fun submitButtonIsDisabledWhenLoading() {
        currentUserLoading.value = true
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Submit").assertIsNotEnabled()
    }

    @Test
    fun submitButtonIsDisabledWhenUserNotLoaded() {
        currentUser.value = ApiResponse.Loading
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Submit").assertIsNotEnabled()
    }
}

