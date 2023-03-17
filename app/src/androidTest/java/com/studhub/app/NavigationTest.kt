import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.AppNavigation
import com.studhub.app.ui.theme.StudHubTheme
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/***
 *
 * this test class is different than HomeScreenTest in the sense
 * that it tests the Navigation from HomeScreen to other screens,
 * not the functionality of HomeScreen
 */
/*
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {


    @get:Rule
    val composeTestRule = createComposeRule()


    @Before
    fun setup() {
        composeTestRule.setContent {
            StudHubTheme {
                AppNavigation()
            }
        }
    }

    @Test
    fun clickAddListing_navigatesToAddListingScreen() {
        composeTestRule.onNodeWithText("Add Listing").assertExists().performClick()

        composeTestRule.onNodeWithText("List your item: ").assertIsDisplayed()

    }

    @Test
    fun clickBrowse_navigatesToBrowseScreen() {
        composeTestRule.onNodeWithText("Browse").assertExists().performClick()

        composeTestRule.onNodeWithText("Browse our selection:").assertIsDisplayed()
    }

    @Test
    fun clickCart_navigatesToCartScreen() {
        composeTestRule.onNodeWithText("Cart").assertExists().performClick()
        composeTestRule.onNodeWithText("Your Cart:").assertIsDisplayed()
    }

    @Test
    fun clickCart_navigatesToAboutScreen() {
        composeTestRule.onNodeWithText("About").assertExists().performClick()
        composeTestRule.onNodeWithText("About Us:").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contact us at ...").assertIsDisplayed()
        composeTestRule.onNodeWithText("We are a MarketPlace company working for EPFL campus.")
            .assertIsDisplayed()
    }


}

 */



