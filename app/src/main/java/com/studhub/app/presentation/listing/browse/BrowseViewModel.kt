package com.studhub.app.presentation.listing.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.domain.usecase.listing.GetListings
import com.studhub.app.domain.usecase.listing.GetListingsBySearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val getListingsBySearch: GetListingsBySearch,
    private val getListings: GetListings,
    private val getCategories: GetCategories
) : ViewModel() {
    private val _listingsState = MutableStateFlow(emptyList<Listing>())
    val listingsState: StateFlow<List<Listing>> = _listingsState
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

    fun searchListings(
        keyword: String,
        minPrice: String,
        maxPrice: String,
        chosenCategories: List<Category>
    ) {
        viewModelScope.launch {
            getListingsBySearch(keyword, minPrice, maxPrice, chosenCategories).collect {
                when (it) {
                    is ApiResponse.Loading -> _listingsState.value = emptyList()
                    is ApiResponse.Failure -> {}
                    is ApiResponse.Success -> _listingsState.value = it.data
                }
            }
        }
    }



    fun getCurrentListings() {
        viewModelScope.launch {
            getListings().collect {
                when (it) {
                    is ApiResponse.Loading -> _listingsState.value = emptyList()
                    is ApiResponse.Failure -> {/*should not fail*/
                    }
                    is ApiResponse.Success -> _listingsState.value = it.data
                }
            }
        }
    }
}

