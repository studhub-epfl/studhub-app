package com.studhub.app.presentation.listing.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.usecase.listing.GetListing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedListingViewModel @Inject constructor(
    private val getListing: GetListing,
    private val startConversationWith: StartConversationWith
) : ViewModel() {
    var currentListing by mutableStateOf<ApiResponse<Listing>>(ApiResponse.Loading)
        private set

    var startConversationWithResponse by mutableStateOf<ApiResponse<Conversation>>(ApiResponse.Loading)
        private set

    fun contactSeller(seller: User, callback: (conversation: Conversation) -> Unit) {
        viewModelScope.launch {
            startConversationWith(seller).collect {
                startConversationWithResponse = it
                when (it) {
                    is ApiResponse.Success -> callback(it.data)
                    else -> {}
                }
            }
        }
    }

    fun fetchListing(id: String) {
        viewModelScope.launch {
            getListing(id).collect {
                currentListing = it
            }
        }
    }
}
