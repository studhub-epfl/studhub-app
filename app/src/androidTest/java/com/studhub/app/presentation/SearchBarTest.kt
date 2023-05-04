package com.studhub.app.presentation.listing.browse.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchBar_textChange_updatesSearchTerm() {
        val searchTerm = mutableStateOf("")
        composeTestRule.setContent {

            SearchBar(
                search = searchTerm,
                onSearch = {}
            )

        }
        val testString = "Hello, World!"

        composeTestRule.onNodeWithText("Search...").performTextInput(testString)

        assertEquals(testString, searchTerm.value)
    }
}
