package com.studhub.app.presentation.listing.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.listing.GetListing
import com.studhub.app.presentation.listing.add.FakeListingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
}