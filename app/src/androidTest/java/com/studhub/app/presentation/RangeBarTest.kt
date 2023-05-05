package com.studhub.app.presentation.listing.browse.components


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4::class)
class RangeBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun rangeBarDisplaysLabel() {


        composeTestRule.setContent {
            RangeBar(
                label = "MIN....CHF",
                search = rememberSaveable { mutableStateOf("") },
                onSearch = {}
                )
        }
        composeTestRule.onNodeWithText("MIN....CHF").performTextInput("5")
    }



}
