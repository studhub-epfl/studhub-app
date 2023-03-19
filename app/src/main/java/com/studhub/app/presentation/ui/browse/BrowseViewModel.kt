package com.studhub.app.presentation.ui.browse

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.studhub.app.DetailedListingView
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.repository.ListingRepositoryImpl
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.listing.GetListings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class BrowseViewModel : ViewModel() {
    private val _listingsState = MutableStateFlow(emptyList<Listing>())
    val listingsState: StateFlow<List<Listing>> = _listingsState

     fun generateSampleListings() {
        val listings = listOf(
            Listing(
                id = "1",
                name = "Algebra for the dummies",
                seller = User(firstName = "Jacky", lastName = "Chan"),
                categories = listOf(Category(name = "Books")),
                description = "Really great book to learn Algebra for entry level readers.",
                price = 34.50F
            ),
            Listing(
                id = "2",
                name = "Brand new Nike Air One",
                seller = User(firstName = "Kristina", lastName = "Gordova"),
                categories = listOf(Category(name = "Clothing")),
                description = "Brand new shoes, full white and ready for any custom work if needed.",
                price = 194.25F
            ),
            Listing(
                id = "3",
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

    fun onListingClick(context: Context, listing: Listing) {
        val intent = Intent(context, DetailedListingView::class.java)
        intent.putExtra("listingTitle", listing.name)
        intent.putExtra("description", listing.description)
        intent.putExtra("category", listing.categories[0].name)
        intent.putExtra("userName", listing.seller.userName)
        intent.putExtra("firstName", listing.seller.firstName)
        intent.putExtra("lastName", listing.seller.lastName)
        intent.putExtra("price", listing.price)
        context.startActivity(intent)
    }
}

