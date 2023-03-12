import androidx.compose.foundation.layout.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.*
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<GreetActivity>()
}
