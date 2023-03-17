package com.studhub.app.presentation.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studhub.app.R

@Composable
fun SignInButton(
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.padding(bottom = 48.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(R.color.purple_700)
        ),
        onClick = onClick
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.ic_launcher_foreground
            ),
            contentDescription = null
        )
        Text(
            text = "Sign in with Google",
            modifier = Modifier.padding(6.dp),
            fontSize = 18.sp
        )
    }
}
