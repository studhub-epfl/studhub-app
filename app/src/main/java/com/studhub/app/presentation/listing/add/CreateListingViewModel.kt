package com.studhub.app.presentation.listing.add

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.ListingType
import com.studhub.app.domain.model.MeetingPoint
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.domain.usecase.listing.CreateListing
import com.studhub.app.domain.usecase.listing.SaveDraftListing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CreateListingViewModel @Inject constructor(
    private val _createListing: CreateListing,
    private val saveDraftListing: SaveDraftListing,
    private val getCategories: GetCategories
) : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    var currentId by mutableStateOf("")
        private set


    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            getCategories().collect {
                when (it) {
                    is ApiResponse.Success -> _categories.value = it.data
                    is ApiResponse.Failure -> { /* should not fail */
                    }
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }

    fun saveDraft(
        title: String,
        description: String,
        categories: List<Category>,
        price: Float,
        meetingPoint: MeetingPoint?,
        pictures: MutableList<Uri>,
    ) {
        val listing = Listing(
            name = title,
            description = description,
            categories = categories,
            price = price,
            meetingPoint = meetingPoint,
            picturesUri = pictures
        )

        viewModelScope.launch {
            _createListing(listing).collect {
                when (it) {
                    is ApiResponse.Success -> { }
                    is ApiResponse.Failure -> { /* should not fail */
                    }
                    is ApiResponse.Loading -> { /* TODO SHOW LOADING ICON */
                    }
                }
            }
        }
    }

    fun createListing(
        title: String,
        description: String,
        categories: List<Category>,
        price: Float,
        meetingPoint: MeetingPoint?,
        pictures: MutableList<Uri>,
        type: ListingType = ListingType.FIXED,
        deadline: Date = Date(),
        callback: (id: String) -> Unit
    ) {
        val listing = Listing(
            name = title,
            description = description,
            categories = categories,
            price = price,
            meetingPoint = meetingPoint,
            picturesUri = pictures,
            type = type,
            biddingDeadline = deadline
        )

        viewModelScope.launch {
            _createListing(listing).collect {
                when (it) {
                    is ApiResponse.Success -> { callback(it.data.id) }
                    is ApiResponse.Failure -> { /* should not fail */
                    }
                    is ApiResponse.Loading -> { /* TODO SHOW LOADING ICON */
                    }
                }
            }
        }
    }
}
