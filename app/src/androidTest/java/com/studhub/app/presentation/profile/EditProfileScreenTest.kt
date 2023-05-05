package com.studhub.app.presentation.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.R
import com.studhub.app.data.repository.MockAuthRepositoryImpl
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.wrapper.EditProfileActivity
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
class EditProfileScreenTest {

    private fun str(id: Int) = composeTestRule.activity.getString(id)

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<EditProfileActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun editProfileScreenTest() {
        // Check if related text is displayed
        composeTestRule.onNodeWithText(str(R.string.profile_edit_title)).assertIsDisplayed()

        /*
        composeTestRule
            .onNodeWithText(str(R.string.profile_edit_form_label_firstname))
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(str(R.string.profile_edit_form_label_lastname))
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(str(R.string.profile_edit_form_label_username))
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(MockAuthRepositoryImpl.loggedInUser.userName)
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(str(R.string.profile_edit_form_label_phone))
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(str(R.string.profile_edit_form_btn_save))
            .performScrollTo()
            .assertIsDisplayed()

         */
    }
}
