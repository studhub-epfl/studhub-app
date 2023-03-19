package com.studhub.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.repository.CategoryRepositoryImpl
import com.studhub.app.data.repository.ListingRepositoryImpl
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.domain.usecase.listing.CreateListing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class CreateListingViewModel : ViewModel(), CreateListingViewModelContract {
    private val categoriesRepository = CategoryRepositoryImpl()
    private val listingRepository = ListingRepositoryImpl()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    override val categories: StateFlow<List<Category>> = _categories


    init {

        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            val getCategories = GetCategories(categoriesRepository)
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

    override fun createListing(
        title: String,
        description: String,
        category: Category,
        price: Float
    ) {
        val listing = Listing(
            id = Random.nextInt().toString(),
            seller = User(userName = "Placeholder Name"),
            name = title,
            description = description,
            categories = listOf(category),
            price = price
        )

        viewModelScope.launch {
            val createListing = CreateListing(listingRepository)
            createListing(listing).collect {
                when (it) {
                    is ApiResponse.Success -> { /* TODO success message and/or return to another view */
                    }
                    is ApiResponse.Failure -> { /* should not fail */
                    }
                    is ApiResponse.Loading -> { /* TODO SHOW LOADING ICON */
                    }
                }
            }
        }
    }
}
