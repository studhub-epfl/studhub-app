import androidx.compose.foundation.layout.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<GreetActivity>()

    @Test
    fun dummyTest() {
        /* TODO */
        Assert.assertEquals(4, (2 + 2).toLong())
    }
}
