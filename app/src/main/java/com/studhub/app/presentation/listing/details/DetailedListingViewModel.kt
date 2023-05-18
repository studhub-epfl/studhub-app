package com.studhub.app.presentation.listing.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.conversation.StartConversationWith
import com.studhub.app.domain.usecase.listing.GetListing
import com.studhub.app.domain.usecase.user.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedListingViewModel @Inject constructor(
    private val getListing: GetListing,
    private val getFavoriteListings: GetFavoriteListings,
    private val addFavoriteListing: AddFavoriteListing,
    private val removeFavoriteListing: RemoveFavoriteListing,
    private val startConversationWith: StartConversationWith,
    private val addBlockedUser: AddBlockedUser,
    private val unblockUser: UnblockUser,
    private val getBlockedUsers: GetBlockedUsers
) : ViewModel() {
    var currentListing by mutableStateOf<ApiResponse<Listing>>(ApiResponse.Loading)
        private set
    var isFavorite = mutableStateOf(false)
    var isBlocked = mutableStateOf(false)

    var startConversationWithResponse by mutableStateOf<ApiResponse<Conversation>>(ApiResponse.Loading)
    
    private val startConversationWith: StartConversationWith
) : ViewModel(), IDetailedListingViewModel {
    override var currentListing by mutableStateOf<ApiResponse<Listing>>(ApiResponse.Loading)
        private set
    override var isFavorite = mutableStateOf(false)
    override var startConversationWithResponse by mutableStateOf<ApiResponse<Conversation>>(ApiResponse.Loading)
        private set

    override fun contactSeller(seller: User, callback: (conversation: Conversation) -> Unit) {
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

    override fun fetchListing(id: String) {
        viewModelScope.launch {
            getListing(id).collect {
                currentListing = it
                if (it is ApiResponse.Success) {
                    getIsFavorite()
                    getIsBlocked()
                }
            }
        }
    }

    override fun getIsFavorite() {
        when (currentListing) {
            is ApiResponse.Success -> {
                val listing = (currentListing as ApiResponse.Success<Listing>).data
                viewModelScope.launch {
                    getFavoriteListings().collect {
                        when (it) {
                            is ApiResponse.Success -> {
                                isFavorite.value = (it.data.contains(listing))
                            }
                            else -> {}
                        }
                    }
                }
            }
            else -> {}
        }
    }

    override fun getIsBlocked() {
        when (currentListing) {
            is ApiResponse.Success -> {
                val listing = (currentListing as ApiResponse.Success<Listing>).data
                viewModelScope.launch {
                    getBlockedUsers().collect {
                        when (it) {
                            is ApiResponse.Success -> {
                                isBlocked.value = (it.data.contains(listing.seller))
                            }
                            else -> {}
                        }
                    }
                }
            }
            else -> {}
        }
    }
    
    override fun onFavoriteClicked() {
        when (currentListing) {
            is ApiResponse.Success -> {
                val listing = (currentListing as ApiResponse.Success<Listing>).data
                viewModelScope.launch {
                    if (!isFavorite.value) {
                        addFavoriteListing(listing).collect {
                            if (it is ApiResponse.Success) {
                                isFavorite.value = true
                            }
                        }
                    } else {
                        removeFavoriteListing(listing).collect {
                            if (it is ApiResponse.Success) {
                                isFavorite.value = false
                            }
                        }
                    }
                }
            }
            else -> {}
        }
    }

    fun onBlockedClicked() {
        when (currentListing) {
            is ApiResponse.Success -> {
                val listing = (currentListing as ApiResponse.Success<Listing>).data
                viewModelScope.launch {
                    if (!isBlocked.value) {
                        addBlockedUser(listing.seller.id).collect {
                            if (it is ApiResponse.Success) {
                                isBlocked.value = true
                            }
                        }
                    } else {
                        unblockUser(listing.seller.id).collect {
                            if (it is ApiResponse.Success) {
                                isBlocked.value = false
                            }
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
