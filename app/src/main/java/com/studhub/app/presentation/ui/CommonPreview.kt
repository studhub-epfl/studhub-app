package com.studhub.app.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.button.PlusButton
import com.studhub.app.presentation.ui.common.input.BasicTextField
import com.studhub.app.presentation.ui.common.input.EmailTextField
import com.studhub.app.presentation.ui.common.input.NumericTextField
import com.studhub.app.presentation.ui.common.input.TextBox
import com.studhub.app.presentation.ui.common.text.BigLabel

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

@Preview(showBackground = true)
@Composable
fun BasicFilledButtonPreview() {
    BasicFilledButton(onClick = { }, label = buttonLabel)
}

@Preview(showBackground = true)
@Composable
fun PlusButtonPreview() {
    PlusButton(onClick = {})
}

/** Inputs preview */

@Preview(showBackground = true)
@Composable
fun BasicTextFieldPreview() {
    BasicTextField(label = fieldLabel)
}

//opens email keyboard
@Preview(showBackground = true)
@Composable
fun EmailTextFieldPreview() {
    EmailTextField(label = fieldLabel)
}

//opens numerical keyboard
@Preview(showBackground = true)
@Composable
fun NumericTextFieldPreview() {
    NumericTextField(label = fieldLabel)
}

@Preview(showBackground = true)
@Composable
fun TextBoxPeview() {
    TextBox(fieldLabel)
}

/** Text elements preview */

@Preview(showBackground = true)
@Composable
fun BigLabelPreview(){
    BigLabel(label = title)
}
