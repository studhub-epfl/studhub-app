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

            OutlinedButton(
                onClick = { navigateToProfileFavorites() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(vertical = 12.dp),
                elevation = ButtonDefaults.buttonElevation(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Text(
                    text = stringResource(R.string.profile_btn_display_favs),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer("large")

            OutlinedButton(
                onClick = { navigateToBlockedUsers() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(vertical = 12.dp),
                elevation = ButtonDefaults.buttonElevation(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Text(
                    text = stringResource(R.string.profile_btn_display_blocked),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer("large")

            OutlinedButton(
                onClick = { navigateToOwnListings() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(vertical = 12.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Text(
                    text = stringResource(R.string.profile_btn_display_own_listings),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
