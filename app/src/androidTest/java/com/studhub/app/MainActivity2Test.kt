import androidx.compose.foundation.layout.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.*
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivity2Test {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity2>()

    @Test
    fun allUiElementExists() {
        composeTestRule.onNodeWithTag("mainGoButton").assertExists()
        composeTestRule.onNodeWithTag("textInput").assertExists()
    }

    @Test
    fun mainGoButtonCreatesAnIntent() {
        val key = "name"
        val value = "testName"

        Intents.init()
        composeTestRule.onNodeWithTag("textInput").performTextInput(value)
        composeTestRule.onNodeWithTag("mainGoButton").performClick()

        intended(
            allOf(
                hasExtra(key, value),
                hasComponent(GreetingActivity2::class.java.name)
            )
        )

        Intents.release()
    }
}