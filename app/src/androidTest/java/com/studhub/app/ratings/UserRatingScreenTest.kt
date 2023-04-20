package com.studhub.app.ratings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.ratings.IUserRatingViewModel
import com.studhub.app.presentation.ratings.UserRatingScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserRatingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun userRatingScreen_displaysRatings() {
        val ratings = listOf(
            Rating(id = "1", reviewerId = "1", thumbUp = true, thumbDown = false, comment = "Great job!"),
            Rating(id = "2", reviewerId = "2", thumbUp = false, thumbDown = true, comment = "Not so good.")
        )

        val currentUser = User(id = "0", firstName = "Current", lastName = "User")
        val targetUser = User(id = "3", firstName = "Target", lastName = "User")
        val reviewer1 = User(id = "1", firstName = "Reviewer", lastName = "One")
        val reviewer2 = User(id = "2", firstName = "Reviewer", lastName = "Two")

        val testViewModel = TestUserRatingViewModel(ApiResponse.Success(currentUser), ApiResponse.Success(ratings))
        testViewModel.testUsers[targetUser.id] = targetUser
        testViewModel.testUsers[reviewer1.id] = reviewer1
        testViewModel.testUsers[reviewer2.id] = reviewer2

        composeTestRule.setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "UserRatingScreen") {
                composable("UserRatingScreen") {
                    UserRatingScreen(targetUserId = targetUser.id, viewModel = testViewModel)
                }
            }
        }

        composeTestRule.onNodeWithText("Current User").assertIsDisplayed()
        composeTestRule.onNodeWithText("Great job!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Not so good.").assertIsDisplayed()
    }
}

// Replace the real ViewModel with a test one
class TestUserRatingViewModel(
    private val testCurrentUser: ApiResponse<User>,
    private val testRatings: ApiResponse<List<Rating>>
) : IUserRatingViewModel {

    private val _currentUser = MutableStateFlow(testCurrentUser)
    override val currentUser: StateFlow<ApiResponse<User>> = _currentUser

    private val _ratings = MutableStateFlow(testRatings)
    override val ratings: StateFlow<ApiResponse<List<Rating>>> = _ratings

    val testUsers = mutableMapOf<String, User>()

    override fun initTargetUser(targetUserId: String) {}

    override suspend fun getUserById(id: String): ApiResponse<User> {
        return testUsers[id]?.let { ApiResponse.Success(it) } ?: ApiResponse.Failure("User not found")
    }

    override fun addRating(userId: String, rating: Rating) {}

    override fun updateRating(userId: String, ratingId: String, rating: Rating) {}

    override fun deleteRating(userId: String, ratingId: String) {}

    override fun getUserRatings(userId: String) {}
}
