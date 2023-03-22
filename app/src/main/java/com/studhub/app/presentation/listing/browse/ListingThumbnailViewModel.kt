package com.studhub.app.presentation.listing.browse

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.studhub.app.domain.model.Listing

class ListingThumbnailViewModel(
    val listing: Listing
) : ViewModel()
