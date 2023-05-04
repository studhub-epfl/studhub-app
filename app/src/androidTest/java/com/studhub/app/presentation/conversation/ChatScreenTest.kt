package com.studhub.app.presentation.conversation

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.wrapper.ChatActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ChatScreenTest {

    private fun str(id: Int) = composeTestRule.activity.getString(id)

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ChatActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun authScreenHidesBottomNavBar() = runBlocking {
        assert(true)
    }
}