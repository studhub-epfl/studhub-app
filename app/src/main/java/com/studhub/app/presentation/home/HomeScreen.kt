package com.studhub.app.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.R
import com.studhub.app.core.Globals
import com.studhub.app.presentation.home.components.*
import com.studhub.app.presentation.ui.common.text.BigLabel
import com.studhub.app.presentation.ui.theme.StudHubTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onAddListingClick: () -> Unit,
    onConversationClick: () -> Unit,
    onBrowseClick: () -> Unit,
    onAboutClick: () -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val user = viewModel.currentUser.collectAsState()
    val scrollState = rememberScrollState()
    Globals.showBottomBar = true;

    StudHubTheme() {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
                .fillMaxSize()
        ) {
            BigLabel(label = stringResource(R.string.home_title))
            Spacer(Modifier.height(16.dp))

            WelcomeText(user = user.value)
            Spacer(Modifier.height(16.dp))

            FeaturedItems()
            Spacer(Modifier.height(160.dp))

            AddListingButton(onClick = onAddListingClick)
            Spacer(Modifier.height(16.dp))

            ConversationButton(onClick = onConversationClick)
            Spacer(Modifier.height(16.dp))

            BrowseButton(onClick = onBrowseClick)
            Spacer(Modifier.height(16.dp))

            CartButton(onClick = onCartClick)
            Spacer(Modifier.height(16.dp))

            ProfileButton(onClick = onProfileClick)
            Spacer(Modifier.height(16.dp))

            AboutButton(onClick = onAboutClick)
        }
    }
}


