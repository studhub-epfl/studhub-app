package com.studhub.app.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.studhub.app.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockCreateListingViewModel : CreateListingViewModelContract {
    private var _categories by mutableStateOf(listOf(Category("Electronics"), Category("Books"), Category("Clothing")))

    override val categories: Flow<List<Category>> = flowOf(_categories)

    override fun createListing(title: String, description: String, category: Category, price: Float) {
        // Do nothing for the mock ViewModel.
    }
}
