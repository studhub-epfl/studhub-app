package com.studhub.app.presentation.auth.verify.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.data.repository.MockAuthRepositoryImpl
import com.studhub.app.presentation.auth.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReloadUserTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun reloadUserCallsCorrectCallback() {
        val authRepo = MockAuthRepositoryImpl()
        val viewModel = AuthViewModel(authRepo)

        runBlocking { viewModel.reloadUser() }
        // delay for dispatch
        runBlocking { delay(50) }

        var clicked = false

        composeTestRule.setContent {
            ReloadUser(viewModel = viewModel, navigateToProfileScreen = { clicked = true })
        }

        assertTrue("navigateToProfileScreen callback should have been called", clicked)
    }
}
