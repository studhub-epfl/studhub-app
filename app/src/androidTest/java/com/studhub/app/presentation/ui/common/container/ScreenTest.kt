package com.studhub.app.presentation.ui.common.container

import androidx.compose.material.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.R
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ScreenTest {

    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun titleIsCorrectlyDisplayed() {
        val title = "Screen title"
        composeTestRule.setContent {
            Screen(title = title) {
                Text(text = "")
            }
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun backButtonIsHiddenWhenParamOnGoBackClickIsNull() {
        val title = "Screen title"
        composeTestRule.setContent {
            Screen(title = title) {
                Text(text = "")
            }
        }

        composeTestRule
            .onNodeWithContentDescription(str(R.string.misc_btn_go_back))
            .assertDoesNotExist()
    }

    @Test
    fun backButtonIsDisplayedWhenParamOnGoBackClickIsNotNull() {
        val title = "Screen title"
        val redirectToSomewhere: () -> Unit = {}
        composeTestRule.setContent {
            Screen(title = title, onGoBackClick = redirectToSomewhere) {
                Text(text = "")
            }
        }

        composeTestRule
            .onNodeWithContentDescription(str(R.string.misc_btn_go_back))
            .assertIsDisplayed()
    }

    @Test
    fun contentIsCorrectlyDisplayed() {
        val title = "Screen title"
        val contentText = "Screen content"
        composeTestRule.setContent {
            Screen(title = title) {
                Text(text = contentText)
            }
        }

        composeTestRule.onNodeWithText(contentText).assertIsDisplayed()
    }

    @Test
    fun contentIsHiddenWhenScreenIsLoading() {
        val title = "Screen title"
        val contentText = "Screen content"
        composeTestRule.setContent {
            Screen(title = title, isLoading = true) {
                Text(text = contentText)
            }
        }

        composeTestRule.onNodeWithText(contentText).assertDoesNotExist()
    }
}
