package com.studhub.app.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.studhub.app.domain.usecase.user.FakeGetCurrentUser
import com.studhub.app.presentation.home.components.*
import com.studhub.app.presentation.ui.common.text.BigLabel
import com.studhub.app.ui.theme.StudHubTheme

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
            modifier = Modifier.padding(16.dp)
                .fillMaxSize()
        ) {
            BigLabel(label = "Home Page")
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

