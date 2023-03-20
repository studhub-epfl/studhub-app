package com.studhub.app.presentation.ui.home//package com.studhub.app.presentation.ui.home
//
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.studhub.app.core.utils.ApiResponse
//import com.studhub.app.domain.model.User
//import com.studhub.app.domain.usecase.user.IGetCurrentUser
//import com.studhub.app.presentation.home.HomeScreen
//import com.studhub.app.presentation.home.HomeViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flowOf
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import javax.inject.Inject
//
//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class HomeTest {
//
//    @get:Rule
//    val hiltRule = HiltAndroidRule(this)
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Inject
//    lateinit var getCurrentUser: IGetCurrentUser
//
//    @Before
//    fun setUp() {
//        hiltRule.inject()
//    }
//
//    @Test
//    fun homeViewModel_getLoggedInUser_setsCurrentUser() {
//        // Given
//        val user = User("123", "John Doe", "john.doe@example.com")
//        val fakeGetCurrentUser = object : IGetCurrentUser {
//            override suspend fun invoke(): Flow<ApiResponse<User>> {
//                return flowOf(ApiResponse.Success(user))
//            }
//        }
//        val viewModel = HomeViewModel(fakeGetCurrentUser)
//
//        // When
//        viewModel.getLoggedInUser()
//
//        // Then
//        assertEquals(user, viewModel.currentUser.value)
//    }
//
//    @Test
//    fun homeScreen_displayedElements() {
//        val viewModel = HomeViewModel(getCurrentUser)
//
//        composeTestRule.setContent {
//            HomeScreen(
//                viewModel = viewModel,
//                onAddListingClick = {},
//                onBrowseClick = {},
//                onAboutClick = {},
//                onCartClick = {}
//            )
//        }
//
//        composeTestRule.onNodeWithText("Home Page").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Welcome").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Featured Items").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Add Listing").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Browse").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Cart").assertIsDisplayed()
//        composeTestRule.onNodeWithText("About").assertIsDisplayed()
//    }
//
//    @Test
//    fun homeScreen_welcomeText_displaysUserName() {
//        val userName = "John Doe"
//        val viewModel = HomeViewModel(getCurrentUser)
//        viewModel._currUser.value = User("123", userName, "john.doe@example.com")
//
//        composeTestRule.setContent {
//            HomeScreen(
//                viewModel = viewModel,
//                onAddListingClick = {},
//                onBrowseClick = {},
//                onAboutClick = {},
//                onCartClick = {}
//            )
//        }
//
//        composeTestRule.onNodeWithText("Welcome, $userName!").assertIsDisplayed()
//    }
//}
