package com.studhub.app.presentation.user

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.ui.theme.StudHubTheme
import com.studhub.app.presentation.user.components.UserCard

@Composable
fun UserThumbnailScreen(
    viewModel: UserThumbnailViewModel,
) {
    UserCard(user = viewModel.user)
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun UserThumbnailPreview() {
    val user = User(
        userName = "Edouard",
    )
    StudHubTheme {
        Column {
            UserCard(user = user)
        }
    }
}
