package com.studhub.app.presentation.listing.details

import android.util.Log
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
    var isFavorite = mutableStateOf(false)

    fun fetchListing(id: String) {
        viewModelScope.launch {
            getListing(id).collect {
                currentListing = it
                if(it is ApiResponse.Success){
                    getIsFavorite()
                }
            }
        }
    }

    private fun getIsFavorite() {
        when (currentListing) {
            is ApiResponse.Success -> {
                val listing = (currentListing as ApiResponse.Success<Listing>).data
                viewModelScope.launch {
                    getFavoriteListings().collect {
                        when (it) {
                            is ApiResponse.Success -> {
                                isFavorite.value = (it.data.contains(listing))
                                Log.d(
                                    "Data favorite",
                                    "Data contains : ${it.data.contains(listing)}"
                                )
                            }
                            else -> {
                                Log.d("Data favorite", "error for getting favorites")
                            }
                        }
                    }
                }
            }
            else -> {}
        }
    }

    fun onFavoriteClicked() {
        when (currentListing) {
            is ApiResponse.Success -> {
                val listing = (currentListing as ApiResponse.Success<Listing>).data
                viewModelScope.launch {
                    if (!isFavorite.value) {
                        addFavoriteListing(listing.id).collect {
                            if (it is ApiResponse.Success) {
                                isFavorite.value = true
                            }
                        }
                    } else {
                        removeFavoriteListing(listing.id).collect {
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
}
