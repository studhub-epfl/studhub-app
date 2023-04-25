package com.studhub.app.presentation.listing.details.components

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.presentation.listing.details.DetailedListingViewModel

@Composable
fun StartConversation(
    viewModel: DetailedListingViewModel = hiltViewModel(),
    navigateToConversation: (conversationId: String) -> Unit,
) {
    when (val startConversationResponse = viewModel.startConversationWithResponse) {
        is ApiResponse.Loading -> {}
        is ApiResponse.Failure -> {}
        is ApiResponse.Success -> {
            navigateToConversation(startConversationResponse.data.id)
        }
    }
}
