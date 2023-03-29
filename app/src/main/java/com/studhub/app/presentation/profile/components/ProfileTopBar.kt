package com.studhub.app.presentation.profile.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.studhub.app.R
import com.studhub.app.presentation.ui.common.button.BasicFilledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(
    signOut: () -> Unit,
    editProfile: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.profile_title)
            )
        },
        actions = {
            BasicFilledButton(
                onClick = editProfile,
                label = stringResource(R.string.profile_btn_edit_profile)
            )

            BasicFilledButton(
                onClick = signOut,
                label = stringResource(R.string.profile_btn_sign_out)
            )
        }
    )
}
