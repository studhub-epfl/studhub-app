package com.studhub.app.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.R
import com.studhub.app.presentation.home.components.*
import com.studhub.app.presentation.ui.common.text.BigLabel
import com.studhub.app.presentation.ui.theme.StudHubTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EarlyEntryPoint

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onAddListingClick: () -> Unit,
    onBrowseClick: () -> Unit,
    onAboutClick: () -> Unit,
    onCartClick: () -> Unit,
) {
    StudHubTheme() {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            BigLabel(label = stringResource(R.string.home_title))
            Spacer(Modifier.height(16.dp))

            WelcomeText(viewModel = viewModel)
            Spacer(Modifier.height(16.dp))

            FeaturedItems()
            Spacer(Modifier.height(160.dp))

            AddListingButton(onClick = onAddListingClick)
            Spacer(Modifier.height(16.dp))

            BrowseButton(onClick = onBrowseClick)
            Spacer(Modifier.height(16.dp))

            CartButton(onClick = onCartClick)
            Spacer(Modifier.height(16.dp))

            AboutButton(onClick = onAboutClick)
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val viewModel = remember {
        HomeViewModel(
            getCurrentUser = FakeGetCurrentUser()
        )
    }

    HomeScreen(
        viewModel = viewModel,
        onAddListingClick = {},
        onBrowseClick = {},
        onAboutClick = {},
        onCartClick = {}
    )
}

 */
