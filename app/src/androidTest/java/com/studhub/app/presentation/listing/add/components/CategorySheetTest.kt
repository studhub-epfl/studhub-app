package com.studhub.app.presentation.listing.add.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.domain.model.Category
import com.studhub.app.presentation.ui.theme.StudHubTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class CategorySheetTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @ExperimentalFoundationApi
    @ExperimentalComposeUiApi
    @Test
    fun categorySheet_isDisplayed() {
        val categories = listOf(
            Category(id = "1", name = "Test Category 1"),
            Category(id = "2", name = "Test Category 2"),
            Category(id = "3", name = "Test Category 3"),
        )
        val chosen = mutableStateListOf<Category>()
        val isOpen = mutableStateOf(true)

        composeTestRule.setContent {
            StudHubTheme {
                Surface {
                    CategorySheet(isOpen, categories, chosen)
                }
            }
        }

        composeTestRule.onNodeWithTag("CategoryItem-Test Category 1").assertExists()
        composeTestRule.onNodeWithTag("CategoryItem-Test Category 2").assertExists()
        composeTestRule.onNodeWithTag("CategoryItem-Test Category 3").assertExists()
    }

    @Test
    fun categoryItem_onClick_isCalled() {
        var wasClicked = false

        // Set the content
        composeTestRule.setContent {
            StudHubTheme {
                CategoryItem("Test Category") {
                    wasClicked = true
                }
            }
        }

        composeTestRule.waitForIdle()

        // Click on the item
        composeTestRule.onNodeWithText("Test Category").performClick()

        composeTestRule.waitForIdle()

        // Assert that the onClick was called
        assert(wasClicked)
    }

    @ExperimentalFoundationApi
    @ExperimentalComposeUiApi
    @Test
    fun categoryItems_doesNotShowChosenCategories() {
        val categories = listOf(
            Category(id = "1", name = "Test Category 1"),
            Category(id = "2", name = "Test Category 2"),
            Category(id = "3", name = "Test Category 3"),
        )
        val chosen = mutableStateListOf<Category>(categories.first())
        val isOpen = mutableStateOf(false)

        composeTestRule.setContent {
            StudHubTheme {
                Surface {
                    CategoryItems(categories, chosen, isOpen)
                }
            }
        }

        composeTestRule.onNodeWithTag("CategoryItem-Test Category 1").assertDoesNotExist()
        composeTestRule.onNodeWithTag("CategoryItem-Test Category 2").assertExists()
        composeTestRule.onNodeWithTag("CategoryItem-Test Category 3").assertExists()
    }
}

