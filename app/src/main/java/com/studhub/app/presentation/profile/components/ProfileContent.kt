package com.studhub.app.presentation.profile.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.studhub.app.R
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.user.GetBlockedUsers
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.misc.Avatar
import com.studhub.app.presentation.ui.common.misc.Spacer
import com.studhub.app.presentation.ui.common.text.BigLabel

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
                .horizontalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer("big")

            Avatar(picture = profile.profilePicture.ifEmpty { null })

            BigLabel(label = profile.userName)

            BasicFilledButton(
                onClick = { navigateToProfileFavorites() },
                label = stringResource(R.string.profile_btn_display_favs)
            )

            Spacer()

            BasicFilledButton(
                onClick = { navigateToBlockedUsers() },
                label = stringResource(R.string.profile_btn_display_blocked)
            )

            Spacer()

            BasicFilledButton(
                onClick = { navigateToOwnListings() },
                label = stringResource(R.string.profile_btn_display_own_listings)
            )
        }
    }
}
