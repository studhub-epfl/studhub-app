package com.studhub.app.presentation.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.studhub.app.R
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.misc.Avatar
import com.studhub.app.presentation.ui.common.misc.Spacer
import com.studhub.app.presentation.ui.common.text.BigLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    padding: PaddingValues,
    profile: User,
    navigateToProfileFavorites: () -> Unit,
    navigateToOwnListings: () -> Unit,
    navigateToBlockedUsers: () -> Unit
) {
    val scrollState = rememberScrollState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer("large")

            Avatar(
                picture = profile.profilePicture.ifEmpty { null },
            )

            Spacer("large")

            BigLabel(label = profile.userName)

            Spacer("large")
            Spacer("large")
            Spacer("large")

            BasicFilledButton(
                onClick = { navigateToProfileFavorites() },
                label = stringResource(R.string.profile_btn_display_favs),
            )

            Spacer()

            BasicFilledButton(
                onClick = { navigateToBlockedUsers() },
                label = stringResource(R.string.profile_btn_display_blocked),
            )

            Spacer()

            BasicFilledButton(
                onClick = { navigateToOwnListings() },
                label = stringResource(R.string.profile_btn_display_own_listings),
            )

            Spacer()

        }
    }
}
