package com.studhub.app.presentation.profile.components

import androidx.compose.foundation.layout.*
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
    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        BigLabel(label = profile.userName)
    }
}
