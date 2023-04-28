package com.studhub.app.presentation.listing.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.usecase.listing.GetListing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedListingViewModel @Inject constructor(
    private val getListing: GetListing
) : ViewModel() {
    var currentListing by mutableStateOf<ApiResponse<Listing>>(ApiResponse.Loading)
        private set

    fun fetchListing(id: String) {
        viewModelScope.launch {
            getListing(id).collect {
                currentListing = it
            }
        }
    }

    fun getMeetingPoint(listingId: String, callback: (LatLng) -> Unit) {
        viewModelScope.launch {
            getListing(listingId).collect {
                when (it) {
                    is ApiResponse.Success -> {
                        val listing = it.data
                        val meetingPoint = listing.meetingPoint
                        if (meetingPoint != null) {
                            callback(LatLng(meetingPoint.latitude, meetingPoint.longitude))
                        }
                    }
                    is ApiResponse.Failure -> { /* handle failure */
                    }
                    is ApiResponse.Loading -> { /* handle loading */
                    }
                }
            }
        }
    }

}
