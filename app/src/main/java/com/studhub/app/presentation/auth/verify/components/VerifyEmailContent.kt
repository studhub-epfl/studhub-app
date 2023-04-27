package com.studhub.app.presentation.auth.verify.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studhub.app.R
import com.studhub.app.presentation.ui.common.misc.Spacer

@Composable
fun VerifyEmailContent(
    padding: PaddingValues,
    reloadUser: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(start = 32.dp, end = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.clickable {
                reloadUser()
            },
            text = stringResource(id = R.string.auth_verify_btn_verified),
            fontSize = 16.sp,
            textDecoration = TextDecoration.Underline
        )

        Spacer("small")

        Text(
            text = stringResource(id = R.string.auth_verify_check_spam),
            fontSize = 15.sp
        )
    }
}
