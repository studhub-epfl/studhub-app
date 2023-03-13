
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.studhub.app.AppNavigation
import com.studhub.app.ui.theme.StudHubTheme
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @BeforeClass
    @Composable
    fun setup() {
        composeTestRule.setContent {
            StudHubTheme {
                AppNavigation()
            }
        }

        @Test
        fun clickAddListing_navigatesToAddListingScreen() {
            composeTestRule.onNodeWithText("Add Listing").assertExists().performClick()
            composeTestRule.onNodeWithText("Browse our selection:").assertExists()
        }

        @Test
        fun clickBrowse_navigatesToBrowseScreen() {
            composeTestRule.onNodeWithText("Browse").assertExists().performClick()
            composeTestRule.onNodeWithText("Browse our selection:").assertExists()
        }


    }}


