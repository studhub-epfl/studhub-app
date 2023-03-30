package com.studhub.app.presentation.listing.browse

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.repository.ListingRepositoryImpl
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.listing.GetListings
import com.studhub.app.domain.usecase.listing.GetListingsBySearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val getListingsBySearch: GetListingsBySearch
) : ViewModel() {
    private val _listingsState = MutableStateFlow(emptyList<Listing>())
    val listingsState: StateFlow<List<Listing>> = _listingsState

    fun searchListings(keyword: String) {
        viewModelScope.launch {
            getListingsBySearch(keyword).collect {
                when (it) {
                    is ApiResponse.Loading -> _listingsState.value = emptyList()
                    is ApiResponse.Failure -> {}
                    is ApiResponse.Success -> _listingsState.value = it.data
                }
            }
        }
    }

     fun generateSampleListings() {
        val listings = listOf(
            Listing(
                id = "33",
                name = "Algebra for the dummies",
                seller = User(firstName = "Jacky", lastName = "Chan"),
                categories = listOf(Category(name = "Books")),
                description = "Really great book to learn Algebra for entry level readers.",
                price = 34.50F
            ),
            Listing(
                id = "32",
                name = "Brand new Nike Air One",
                seller = User(firstName = "Kristina", lastName = "Gordova"),
                categories = listOf(Category(name = "Clothing")),
                description = "Brand new shoes, full white and ready for any custom work if needed.",
                price = 194.25F
            ),
            Listing(
                id = "31",
                name = "Super VTT 2000 with custom paint",
                seller = User(firstName = "Marc", lastName = "Marquez"),
                categories = listOf(Category(name = "Mobility")),
                description = "Robust bike for downhill riding and will " +
                        "look absolutely unique with this custom paint work",
                price = 1500F
            )
        )

        _listingsState.value = listings


    }

    suspend fun getAllListings() {
        val listingRepository = ListingRepositoryImpl()
        val getListings = GetListings(listingRepository)

        getListings().collect {
            when (it) {
                is ApiResponse.Success -> {
                    val data = it.data
                    _listingsState.value = data
                }
                is ApiResponse.Failure -> {
                    // TODO - show error status on screen
                }
                is ApiResponse.Loading -> {}
            }
        }
    }




}

