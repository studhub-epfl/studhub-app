package com.studhub.app.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.button.PlusButton
import com.studhub.app.presentation.ui.common.input.*
import com.studhub.app.presentation.ui.common.misc.Avatar
import com.studhub.app.presentation.ui.common.misc.LoadingCircle
import com.studhub.app.presentation.ui.common.text.BigLabel
import com.studhub.app.presentation.ui.common.text.TextChip
import com.studhub.app.presentation.ui.theme.StudHubTheme

private var buttonLabel = "This is a button"
private var fieldLabel = "This is a field"
private var title = "This is some title"

/**
 * This is a preview file showing all the common UI composables created for the project.
 * Any new created composables meant to be commonly used throughout the app needs to have
 * a preview here.
 *
 * If a composable require a change please suggest it to the team so we can plan eventual
 * refactoring of said composable.
 */

/** Buttons preview */
@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun BasicFilledButtonPreview() {
    StudHubTheme() {
        BasicFilledButton(onClick = { }, label = buttonLabel)
    }
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun PlusButtonPreview() {
    StudHubTheme() {
        PlusButton(onClick = {})
    }
}

/** Inputs preview */
@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun BasicTextFieldPreview() {
    StudHubTheme() {
        BasicTextField(label = fieldLabel)
    }
}

//opens email keyboard
@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun EmailTextFieldPreview() {
    EmailTextField(email = TextFieldValue(""), onEmailValueChange = {})
}

//opens password keyboard
@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun PasswordTextFieldPreview() {
    PasswordTextField(password = TextFieldValue(""), onPasswordValueChange = {})
}

//opens numerical keyboard
@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun NumericTextFieldPreview() {
    StudHubTheme() {
        NumericTextField(label = fieldLabel)
    }
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun TextBoxPreview() {
    StudHubTheme() {
        TextBox(fieldLabel)
    }
}

/** Text elements preview */
@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun BigLabelPreview() {
    StudHubTheme() {
        BigLabel(label = title)
    }
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun TextChipPreview() {
    TextChip(label = "Some text")
}

/** Misc elements preview */
@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun ProgressBarPreview() {
    StudHubTheme {
        LoadingCircle()
    }
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun AvatarPreview() {
    Avatar(picture = null)
}








