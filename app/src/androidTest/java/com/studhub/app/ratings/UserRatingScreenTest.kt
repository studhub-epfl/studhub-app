package com.studhub.app.ratings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.local.dao.UserDao
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.ratings.IUserRatingViewModel
import com.studhub.app.wrapper.RatingActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UserRatingScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<RatingActivity>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    @Inject
    lateinit var viewModel: IUserRatingViewModel

    @Inject
    lateinit var userDao: UserDao

    @Before
    fun setUp() {
        hiltRule.inject()

        val userId = "userId"

        val user = User(userId)


        runBlocking {
            userDao.insertUser(user)
        }
    }

    @Test
    fun userHeaderIsDisplayed() {
        composeTestRule.onNodeWithTag("UserHeader").assertIsDisplayed()
    }

    @Test
    fun addRatingButtonIsDisplayed() {

        composeTestRule.onNodeWithTag("AddRatingButton").assertIsDisplayed()
    }

    @Test
    fun ratingsListIsDisplayed() {
        composeTestRule.onNodeWithTag("RatingsList").assertIsDisplayed()
    }


    @Test
    fun testInitTargetUser() = run {
        val userId = "userId"
        runBlocking {
            viewModel.initTargetUser(userId)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testAddRating() = testDispatcher.run {
        val targetUserId = "1234"
        val ratingText = "Nice work!"
        val thumbUp = true
        val thumbDown = false
        val rating = Rating(
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

        viewModel.addRating(targetUserId, rating)

        verify(viewModel).addRating(targetUserId, rating)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testUpdateRating() = testDispatcher.run {

        val targetUserId = "1234"
        val ratingText = "Nice work!"
        val thumbUp = true
        val thumbDown = false
        val rating = Rating(
            id = "3",
            userId = targetUserId,
            reviewerId = "1",
            firstName = "John",
            lastName = "Doe",
            thumbUp = thumbUp,
            thumbDown = thumbDown,
            comment = ratingText,
            timestamp = System.currentTimeMillis()
        )

        val rating2 = Rating(
            id = "1",
            userId = targetUserId,
            reviewerId = "1",
            firstName = "Johner",
            lastName = "Doeer",
            thumbUp = thumbUp,
            thumbDown = thumbDown,
            comment = ratingText,
            timestamp = System.currentTimeMillis()
        )

        viewModel.addRating(targetUserId, rating2)


        viewModel.updateRating(targetUserId, "1", rating)

        verify(viewModel).updateRating(targetUserId, "1", rating)


    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testDeleteRating() = testDispatcher.run {
        val targetUserId = "1234"
        val ratingText = "Nice work!"
        val thumbUp = true
        val thumbDown = false
        val rating = Rating(
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

        viewModel.addRating(targetUserId, rating)

        viewModel.deleteRating(targetUserId, "1")

        verify(viewModel).deleteRating(targetUserId, "1")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetUserRatings() = testDispatcher.run {
        val userId = "1234"

        viewModel.getUserRatings(userId)

        verify(viewModel).getUserRatings(userId)
    }

    @Test
    fun testGetUserById() {
        val userId = "userId"

        var response: ApiResponse<User>?
        runBlocking {
            response = viewModel.getUserById(userId)
        }

        when (response) {
            is ApiResponse.Success -> {
            }

            is ApiResponse.Failure -> {
            }

            is ApiResponse.Loading -> {

            }

            else -> {
            }
        }
    }
}
