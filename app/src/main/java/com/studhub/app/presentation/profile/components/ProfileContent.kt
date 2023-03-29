package com.studhub.app.presentation.profile.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.ui.common.text.BigLabel

@Composable
fun ProfileContent(
    padding: PaddingValues,
    profile: User
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .horizontalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        BigLabel(label = profile.userName)
    }
}
