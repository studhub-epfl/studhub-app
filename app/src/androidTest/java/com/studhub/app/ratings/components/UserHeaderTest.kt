package com.studhub.app.ratings.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.ratings.components.UserHeader
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserHeaderTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var targetUser: ApiResponse<User>

    @Before
    fun setUp() {
        val user = User(firstName = "Test", lastName = "User")
        targetUser = ApiResponse.Success(user)
    }

    @Test
    fun whenApiResponseIsSuccess_displayUserName() {
        composeTestRule.setContent {
            UserHeader(targetUser = mutableStateOf(targetUser), thumbsUpCount = 5, thumbsDownCount = 3)
        }

        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
    }

    @Test
    fun whenApiResponseIsSuccess_displayThumbsUpCount() {
        composeTestRule.setContent {
            UserHeader(targetUser = mutableStateOf(targetUser), thumbsUpCount = 5, thumbsDownCount = 3)
        }

        composeTestRule.onNodeWithTag("ThumbsUpCount").assertIsDisplayed()
    }

    @Test
    fun whenApiResponseIsSuccess_displayThumbsDownCount() {
        composeTestRule.setContent {
            UserHeader(targetUser = mutableStateOf(targetUser), thumbsUpCount = 5, thumbsDownCount = 3)
        }

        composeTestRule.onNodeWithTag("ThumbsDownCount").assertIsDisplayed()
    }

    @Test
    fun whenApiResponseIsError_displayErrorText() {
        val errorResponse: ApiResponse<User> = ApiResponse.Failure("Error message")
        composeTestRule.setContent {
            UserHeader(targetUser = mutableStateOf(errorResponse), thumbsUpCount = 5, thumbsDownCount = 3)
        }

        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
    }

    @Test
    fun whenApiResponseIsLoading_displayCircularProgressIndicator() {
        val loadingResponse: ApiResponse<User> = ApiResponse.Loading
        composeTestRule.setContent {
            UserHeader(targetUser = mutableStateOf(loadingResponse), thumbsUpCount = 5, thumbsDownCount = 3)
        }

        composeTestRule.onNodeWithTag("loading").assertExists()
    }
}
