package com.studhub.app.presentation.listing.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User

interface IDetailedListingViewModel {
    val currentListing: ApiResponse<Listing>
    val isFavorite: State<Boolean>
    val isBlocked: State<Boolean>
    val startConversationWithResponse: ApiResponse<Conversation>
    val userId: String

    fun contactSeller(seller: User, callback: (conversation: Conversation) -> Unit)
    fun fetchListing(id: String)
    fun placeBid(bid: Float?, onError: (msg: String) -> Unit)
    fun onFavoriteClicked()
    fun onBlockedClicked()
    fun getIsFavorite()
    fun getIsBlocked()
}
