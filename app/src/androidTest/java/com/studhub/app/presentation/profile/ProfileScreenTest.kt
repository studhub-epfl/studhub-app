package com.studhub.app.presentation.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.R
import com.studhub.app.data.repository.MockAuthRepositoryImpl
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.wrapper.ProfileActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    private fun str(id: Int) = composeTestRule.activity.getString(id)

    @Inject
    lateinit var authRepositoryImpl: AuthRepository

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ProfileActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun profileScreenTest() {
        // Check if related text is displayed
        composeTestRule.onNodeWithText(str(R.string.profile_title)).assertIsDisplayed()

        composeTestRule.onNodeWithText(str(R.string.profile_btn_sign_out)).assertIsDisplayed()

        composeTestRule.onNodeWithText(str(R.string.profile_btn_display_favs)).assertIsDisplayed()

        composeTestRule.onNodeWithText(str(R.string.profile_btn_display_blocked)).assertIsDisplayed()

        composeTestRule.onNodeWithText(MockAuthRepositoryImpl.loggedInUser.userName)
            .assertIsDisplayed()
    }

    @Test
    fun signOutButtonSignsOutInRepo() {
        assertTrue(authRepositoryImpl.isUserAuthenticatedInFirebase)

        composeTestRule.onNodeWithText(str(R.string.profile_btn_sign_out)).performClick()

        assertFalse(authRepositoryImpl.isUserAuthenticatedInFirebase)
    }

}
