package com.studhub.app.presentation.profile.components

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.studhub.app.R
import com.studhub.app.core.utils.isValidSwissPhoneNumber
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.input.BasicTextField
import com.studhub.app.presentation.ui.common.input.ImagePicker
import com.studhub.app.presentation.ui.common.input.NumericTextField
import com.studhub.app.presentation.ui.common.misc.Avatar
import com.studhub.app.presentation.ui.common.misc.Spacer

@Composable
fun EditProfileForm(
    firstName: MutableState<String>,
    lastName: MutableState<String>,
    userName: MutableState<String>,
    phoneNumber: MutableState<String>,
    profilePictureUri: MutableState<Uri?>,
    profilePicture: String,
    onSubmit: () -> Unit
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Avatar(picture = profilePictureUri.value ?: profilePicture)
            Spacer("small")
            ImagePicker(onNewPicture = { profilePictureUri.value = it })

            Spacer("large")

            BasicTextField(stringResource(R.string.profile_edit_form_label_firstname), firstName)
            BasicTextField(stringResource(R.string.profile_edit_form_label_lastname), lastName)
            BasicTextField(stringResource(R.string.profile_edit_form_label_username), userName)
            NumericTextField(stringResource(R.string.profile_edit_form_label_phone), phoneNumber)


            // Display an error message if the phone number is not valid
            val isPhoneNumberValid = isValidSwissPhoneNumber(phoneNumber.value)
            if (!isPhoneNumberValid) {
                Text(
                    text = stringResource(R.string.profile_edit_form_error_invalid_phone),
                    modifier = Modifier.padding(4.dp),
                    color = Color.Red
                )
            }

            Spacer(Modifier.size(10.dp))

            // Disable the button if the phone number is not a valid Swiss number
            Button(
                onClick = {
                    if (isPhoneNumberValid) onSubmit()
                },
                enabled = isPhoneNumberValid,
                modifier = Modifier.padding(top = 3.dp, bottom = 3.dp)
            ) {
                Text(stringResource(R.string.profile_edit_form_btn_save))
            }

        }
    }
}


