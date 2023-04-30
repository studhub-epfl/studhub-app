package com.studhub.app.presentation.listing.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

@HiltViewModel
class CreateListingViewModel @Inject constructor(
    private val _createListing: CreateListing,
    private val getCategories: GetCategories,

) : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _navigateToListing = MutableLiveData<String>()
    val navigateToListing: LiveData<String> = _navigateToListing

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
        category: Category,
        price: Float,
        meetingPoint: MeetingPoint?,
        callback: (id: String) -> Unit
    ) {
        val listing = Listing(
            name = title,
            description = description,
            categories = listOf(category),
            price = price,
            meetingPoint = meetingPoint
        )

        viewModelScope.launch {
            Log.d("CreateListingViewModel", "Before createListing call")
            _createListing(listing).collect {
                when (it) {
                    is ApiResponse.Success -> {
                        callback(it.data.id)
                    }
                    is ApiResponse.Failure -> { /* should not fail */
                    }
                    is ApiResponse.Loading -> { /* TODO SHOW LOADING ICON */
                    }
                }
            }
            Log.d("CreateListingViewModel", "After createListing call")
        }
    }
}
