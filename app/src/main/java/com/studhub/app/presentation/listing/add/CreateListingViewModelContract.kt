package com.studhub.app.presentation.listing.add

import com.studhub.app.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CreateListingViewModelContract {
    val categories: Flow<List<Category>>
    fun createListing(title: String, description: String, category: Category, price: Float)
}
