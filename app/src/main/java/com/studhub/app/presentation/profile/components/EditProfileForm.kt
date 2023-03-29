package com.studhub.app.presentation.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.studhub.app.R
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.input.BasicTextField
import com.studhub.app.presentation.ui.common.input.NumericTextField

@Composable
fun EditProfileForm(
    firstName: MutableState<String>,
    lastName: MutableState<String>,
    userName: MutableState<String>,
    phoneNumber: MutableState<String>,
    onSubmit: () -> Unit
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicTextField(stringResource(R.string.profile_edit_form_label_firstname), firstName)
            BasicTextField(stringResource(R.string.profile_edit_form_label_lastname), lastName)
            BasicTextField(stringResource(R.string.profile_edit_form_label_username), userName)
            NumericTextField(stringResource(R.string.profile_edit_form_label_phone), phoneNumber)
            Spacer(Modifier.size(10.dp))
            BasicFilledButton(
                onClick = onSubmit,
                label = stringResource(R.string.profile_edit_form_btn_save)
            )
        }
    }
}