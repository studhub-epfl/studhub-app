package com.studhub.app.presentation.home

import AddListingButton
import WelcomeText
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
    onProfileClick: () -> Unit
) {
    val user = viewModel.currentUser.collectAsState()
    Globals.showBottomBar = true

    val titleTextStyle = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.home_title),
                style = titleTextStyle,
                modifier = Modifier.padding(bottom = 26.dp)
            )

            WelcomeText(user = user.value)
        }

        Spacer(modifier = Modifier.height(45.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddListingButton(onClick = onAddListingClick, Modifier.fillMaxWidth())
            ConversationButton(onClick = onConversationClick, Modifier.fillMaxWidth())
            BrowseButton(onClick = onBrowseClick, Modifier.fillMaxWidth())
            ProfileButton(onClick = onProfileClick, Modifier.fillMaxWidth())
            AboutButton(onClick = onAboutClick, Modifier.fillMaxWidth())
        }
    }
}
