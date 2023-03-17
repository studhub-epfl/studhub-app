package com.studhub.app.presentation.home


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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

    val user = viewModel.currentUser.collectAsState()

    StudHubTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            BigLabel(label = "Home Page")
            Spacer(Modifier.height(16.dp))

            Text(
                text =
                if (user.value == null)
                    "Welcome to our app!"
                else
                    "Welcome to our app ${user.value!!.userName}!"
            )

            Spacer(Modifier.height(16.dp))
            Text(text = "Here are some featured items:")
            Spacer(Modifier.height(16.dp))
            Text(text = "//FILL")
            Spacer(Modifier.height(160.dp))

            Button(
                onClick = onAddListingClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add Listing")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onBrowseClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Browse")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCartClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cart")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onAboutClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "About")
            }

        }
    }
}
