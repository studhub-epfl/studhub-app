import androidx.compose.foundation.layout.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetActivity2Test {
    /*
    TODO - find a nice way to setup composeTestRule with a custom intent instead of this workaround
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity2>()

    @Test
    fun greetActivityGivesCorrectMessageFromIntentExtra() {
        val value = "testName"
        val text = "Hello testName!"

        Intents.init()

        composeTestRule.onNodeWithTag("textInput").performTextInput(value)
        composeTestRule.onNodeWithTag("mainGoButton").performClick()
        composeTestRule.onNodeWithTag("greetingMessage").assertTextEquals(text)

        Intents.release()
    }
}