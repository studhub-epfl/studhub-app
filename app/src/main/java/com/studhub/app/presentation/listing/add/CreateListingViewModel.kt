package com.studhub.app.presentation.listing.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.MeetingPoint
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.domain.usecase.listing.CreateListing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CreateListingViewModel @Inject constructor(
    private val _createListing: CreateListing,
    private val getCategories: GetCategories
) : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories


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

    fun createListing(
        title: String,
        description: String,
        categories: List<Category>,
        price: Float,
        meetingPoint: MeetingPoint?,
        pictures: MutableList<Uri>,
        callback: (id: String) -> Unit
    ) {
        val listing = Listing(
            name = title,
            description = description,
            categories = categories,
            price = price,
            meetingPoint = meetingPoint
            picturesUri = pictures
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
