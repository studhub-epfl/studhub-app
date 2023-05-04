package com.studhub.app.presentation.listing.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.usecase.listing.GetListing
import com.studhub.app.domain.usecase.user.AddFavoriteListing
import com.studhub.app.domain.usecase.user.GetFavoriteListings
import com.studhub.app.domain.usecase.user.RemoveFavoriteListing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedListingViewModel @Inject constructor(
    private val getListing: GetListing,
    private val getFavoriteListings: GetFavoriteListings,
    private val addFavoriteListing: AddFavoriteListing,
    private val removeFavoriteListing: RemoveFavoriteListing
) : ViewModel() {
    var currentListing by mutableStateOf<ApiResponse<Listing>>(ApiResponse.Loading)
        private set
    private val _userFavorites = MutableSharedFlow<List<Listing>>(replay = 0)
    var isFavorite = mutableStateOf(false)

    fun fetchListing(id: String) {
        viewModelScope.launch {
            getListing(id).collect {
                currentListing = it
            }
        }
    }

    fun getFavorites() {
        when (currentListing) {
            is ApiResponse.Success -> {
                val listing = (currentListing as ApiResponse.Success<Listing>).data
                viewModelScope.launch {
                    getFavoriteListings().collect {
                        when (it) {
                            is ApiResponse.Success -> {
                                _userFavorites.emit(it.data)
                                isFavorite.value = (it.data.contains(listing))
                            }
                            else -> _userFavorites.emit(emptyList())
                        }
                    }
                }
            }
            else -> {}
        }
    }

    fun onFavoriteClicked(isFavorite: Boolean) {
        when (currentListing) {
            is ApiResponse.Success -> {
                val listing = (currentListing as ApiResponse.Success<Listing>).data
                viewModelScope.launch {
                    if (isFavorite) {
                        addFavoriteListing(listing.id)
                    } else {
                        removeFavoriteListing(listing.id)
                    }
                }
            }
            else -> {}
        }
    }
}
