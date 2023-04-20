package com.studhub.app.ratings
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.usecase.user.*
import com.studhub.app.presentation.ratings.UserRatingViewModel
import com.studhub.app.wrapper.ProfileActivity
import com.studhub.app.wrapper.RatingActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UserRatingViewModelTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<RatingActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @OptIn(ExperimentalCoroutinesApi::class)
    val testScope = TestScope()

    private val addRatingUseCase = mock(AddRating::class.java)
    private val updateRatingUseCase = mock(UpdateRating::class.java)
    private val deleteRatingUseCase = mock(DeleteRating::class.java)
    private val getUserRatingsUseCase = mock(GetUserRatings::class.java)
    private val getUser = mock(GetUser::class.java)
    private val getCurrentUser = mock(GetCurrentUser::class.java)

    private val viewModel = UserRatingViewModel(
        addRatingUseCase,
        updateRatingUseCase,
        deleteRatingUseCase,
        getUserRatingsUseCase,
        getUser,
        getCurrentUser
    )

    @Test
    fun test_addRating_updates_ratings() = testScope.runTest {
        val userId = "testUserId"
        val rating = Rating(id = "ratingId", reviewerId = "reviewerId", thumbUp = true, thumbDown = false, comment = "Great!")
        val response = ApiResponse.Success(listOf(rating))

        `when`(addRatingUseCase(userId, rating)).thenReturn(flowOf(ApiResponse.Success(rating)))
        `when`(getUserRatingsUseCase(userId)).thenReturn(flowOf(response))

        viewModel.addRating(userId, rating)

        assert(viewModel.ratings.value is ApiResponse.Success)
        assert((viewModel.ratings.value as ApiResponse.Success).data.contains(rating))
    }

    @Test
    fun test_updateRating_updates_ratings() = testScope.runTest {
        val userId = "testUserId"
        val ratingId = "ratingId"
        val rating = Rating(id = ratingId, reviewerId = "reviewerId", thumbUp = true, thumbDown = false, comment = "Great!")
        val updatedRating = rating.copy(comment = "Updated comment")
        val response = ApiResponse.Success(listOf(updatedRating))

        `when`(updateRatingUseCase(userId, ratingId, updatedRating)).thenReturn(flowOf(ApiResponse.Success(updatedRating)))
        `when`(getUserRatingsUseCase(userId)).thenReturn(flowOf(response))

        viewModel.updateRating(userId, ratingId, updatedRating)

        assert(viewModel.ratings.value is ApiResponse.Success)
        assert((viewModel.ratings.value as ApiResponse.Success).data.contains(updatedRating))
    }

    @Test
    fun test_deleteRating_updates_ratings() = testScope.runTest {
        val userId = "testUserId"
        val ratingId = "ratingId"
        val rating = Rating(id = ratingId, reviewerId = "reviewerId", thumbUp = true, thumbDown = false, comment = "Great!")
        val response = ApiResponse.Success(emptyList<Rating>())

        `when`(deleteRatingUseCase(userId, ratingId)).thenReturn(flowOf(ApiResponse.Success(true)))
        `when`(getUserRatingsUseCase(userId)).thenReturn(flowOf(response))

        viewModel.deleteRating(userId, ratingId)

        assert(viewModel.ratings.value is ApiResponse.Success)
        assert((viewModel.ratings.value as ApiResponse.Success).data.isEmpty())
    }
}

