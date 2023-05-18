package com.studhub.app.ratings.components

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.local.dao.UserDao
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.ratings.IUserRatingViewModel
import com.studhub.app.presentation.ratings.components.handleSubmitRating
import com.studhub.app.wrapper.RatingActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import java.util.*
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HandleSubmitRating {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<RatingActivity>()

    @Inject
    lateinit var viewModel: IUserRatingViewModel


    private lateinit var userResponse: ApiResponse<User>
    private lateinit var currentUser: MutableState<ApiResponse<User>>
    private val thumbsUpCount = mutableStateOf(0)
    private val thumbsDownCount = mutableStateOf(0)
    fun <T> nullableAny(): T = Mockito.any<T>()

    @Before
    fun setup() {
        hiltRule.inject()
        userResponse = ApiResponse.Success(
            User(
                id = "1",
                firstName = "John",
                lastName = "Doe"
            )
        )
        currentUser = mutableStateOf(userResponse)
    }


    @Test
    fun testAddRating_whenCurrentRatingIsNull() {
        val targetUserId = "1234"
        val ratingText = "Nice work!"
        val thumbUp = true
        val thumbDown = false
        val currentRating: Rating? = null

        handleSubmitRating(
            viewModel,
            targetUserId,
            currentUser,
            thumbUp,
            ratingText,
            currentRating,
            thumbsUpCount,
            thumbsDownCount,
            thumbDown
        )

        Thread.sleep(1000)
        verify(viewModel).addRating(nullableAny(), nullableAny())
        verify(viewModel).getUserRatings(targetUserId)
        assertEquals(1, thumbsUpCount.value)
        assertEquals(0, thumbsDownCount.value)
    }

    @Test
    fun testUpdateRating_whenCurrentRatingIsNotNull() {
        val targetUserId = "1234"
        val ratingText = "Nice work!"
        val thumbUp = true
        val thumbDown = false
        val currentRating = Rating(
            id = "1",
            userId = targetUserId,
            reviewerId = "1",
            firstName = "John",
            lastName = "Doe",
            thumbUp = thumbUp,
            thumbDown = thumbDown,
            comment = ratingText,
            timestamp = System.currentTimeMillis()
        )

        handleSubmitRating(
            viewModel,
            targetUserId,
            currentUser,
            thumbUp,
            ratingText,
            currentRating,
            thumbsUpCount,
            thumbsDownCount,
            thumbDown
        )

        Thread.sleep(1000)

        verify(viewModel).updateRating(
            targetUserId,
            currentRating.id,
            currentRating.copy(thumbUp = thumbUp, thumbDown = !thumbUp, comment = ratingText)
        )
        verify(viewModel).getUserRatings(targetUserId)
        assertEquals(1, thumbsUpCount.value)
        assertEquals(0, thumbsDownCount.value)
    }

    @Test
    fun testNothingHappens_whenCurrentUserIsNotSuccess() {
        val targetUserId = "1234"
        val ratingText = "Nice work!"
        val thumbUp = true
        val thumbDown = false
        val currentRating: Rating? = null
        currentUser.value = ApiResponse.Loading

        handleSubmitRating(
            viewModel,
            targetUserId,
            currentUser,
            thumbUp,
            ratingText,
            currentRating,
            thumbsUpCount,
            thumbsDownCount,
            thumbDown
        )

        Thread.sleep(1000)
        verify(viewModel, never()).addRating(nullableAny(), nullableAny())
        verify(viewModel, never()).updateRating(nullableAny(), nullableAny(), nullableAny())
        verify(viewModel, never()).getUserRatings(nullableAny())
    }
}
