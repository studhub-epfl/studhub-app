import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studhub.app.R
import com.studhub.app.domain.model.User
import java.time.format.TextStyle

@Composable
fun WelcomeText(user: User?, modifier: Modifier = Modifier) {
    val welcomeText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Black, fontSize = 24.sp)) {  // Modify color and size to your preference
            append(
                if (user == null)
                    stringResource(R.string.home_welcome_message)
                else
                    String.format(
                        stringResource(R.string.home_welcome_name_message),
                        user.userName
                    )
            )
        }
    }
    Text(
        text = welcomeText,
        modifier = modifier
    )
}
