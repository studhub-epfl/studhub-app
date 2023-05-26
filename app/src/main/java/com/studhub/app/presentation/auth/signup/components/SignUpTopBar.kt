package com.studhub.app.presentation.auth.signup.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.studhub.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.auth_signup_title)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.misc_btn_go_back),
                )
            }
        }
    )
}
