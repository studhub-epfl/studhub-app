package com.studhub.app.presentation.profile

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.R
import com.studhub.app.presentation.profile.components.UserCard
import com.studhub.app.presentation.ui.common.text.BigLabel

@Composable
fun ProfileBlockedScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val blocked = viewModel.blockedUsers.collectAsState(initial = emptyList())
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.getBlocked()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BigLabel(label = stringResource(R.string.profile_blocked_title))

        if (blocked.value.isEmpty()) {
            BigLabel(label = stringResource(R.string.profile_blocked_no_blocked))
        } else {
            LazyColumn {
                items(blocked.value) { user ->
                    Spacer(modifier = Modifier.height(6.dp))
                    UserCard(
                        user = user,
                        isBlocked = true, // users are in the blockedUsers list, so this should always be true
                        onBlockClicked = { viewModel.unblockUser(user) } // cf my comment on `onBlockedClicked`
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Divider()
                }
            }
        }
    }

}

