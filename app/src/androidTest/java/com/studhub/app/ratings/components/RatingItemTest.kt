package com.studhub.app.ratings.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.ratings.components.RatingItem
import com.studhub.app.presentation.ratings.components.UserHeader
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RatingItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var rating: Rating
    private lateinit var reviewer: User

    @Before
    fun setup() {
        rating = Rating(
            id = "1",
            userId = "123",
            reviewerId = "456",
            firstName = "John",
            lastName = "Doe",
            thumbUp = true,
            thumbDown = false,
            comment = "Great job!",
            timestamp = System.currentTimeMillis()
        )

        reviewer = User(
            id = "456",
            firstName = "Jane",
            lastName = "Doe"
        )
    }

    @Test
    fun showsReviewerName() {
        composeTestRule.setContent {
            RatingItem(rating, reviewer, false, {}, {})
        }

        composeTestRule.onNodeWithText("${reviewer.firstName} ${reviewer.lastName}")
            .assertIsDisplayed()
    }

    @Test
    fun showsComment() {
        composeTestRule.setContent {
            RatingItem(rating, reviewer, false, {}, {})
        }

        composeTestRule.onNodeWithText(rating.comment)
            .assertIsDisplayed()
    }

    @Test
    fun showsEditAndRemoveButtonsWhenIsCurrentUserRating() {
        composeTestRule.setContent {
            RatingItem(rating, reviewer, true, {}, {})
        }

        composeTestRule.onNodeWithText("Edit Rating")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Remove Rating")
            .assertIsDisplayed()
    }

    @Test
    fun doesNotShowEditAndRemoveButtonsWhenNotCurrentUserRating() {
        composeTestRule.setContent {
            RatingItem(rating, reviewer, false, {}, {})
        }

        composeTestRule.onNodeWithText("Edit Rating")
            .assertDoesNotExist()

        composeTestRule.onNodeWithText("Remove Rating")
            .assertDoesNotExist()
    }

    @Test
    fun showsThumbsDownWhenRatingIsThumbDown() {
        val thumbDownRating = rating.copy(thumbUp = false)

        composeTestRule.setContent {
            RatingItem(thumbDownRating, reviewer, false, {}, {})
        }

        composeTestRule.onNodeWithContentDescription("Thumbs Down")
            .assertIsDisplayed()
    }


}

