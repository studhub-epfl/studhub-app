package com.studhub.app.presentation.listing.add

import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.R
import com.studhub.app.core.utils.Utils.Companion.displayMessage
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.ListingType
import com.studhub.app.domain.model.MeetingPoint
import com.studhub.app.presentation.listing.add.components.CategorySheet
import com.studhub.app.presentation.listing.add.components.ListingForm
import com.studhub.app.presentation.ui.common.container.Screen
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListingScreen(
    viewModel: CreateListingViewModel = hiltViewModel(),
    navigateToListing: (id: String) -> Unit,
    navigateBack: () -> Unit,
    draftId: String? = null
) {

    val categories by viewModel.categories.collectAsState(emptyList())
    val title = rememberSaveable { mutableStateOf("") }
    val description = rememberSaveable { mutableStateOf("") }
    val price = rememberSaveable { mutableStateOf("") }
    val meetingPoint = remember { mutableStateOf<MeetingPoint?>(null) }
    val pictures = rememberSaveable { mutableListOf<Uri>() }
    val chosenCategories = remember { mutableStateListOf<Category>() }
    val openCategorySheet = rememberSaveable { mutableStateOf(false) }
    val switch = rememberSaveable { mutableStateOf(false) }
    // special state used for the date picker, there is no .value() property but instead
    // there is a .selectedDateMillis which is a Long typed timestamp
    val date: DatePickerState = rememberDatePickerState()

    val context = LocalContext.current
    val msg = stringResource(R.string.listings_add_feedback_draft_saved)

    val scrollState = rememberScrollState()

    // if there is a given draft id, fetch the corresponding draft and display its values
    LaunchedEffect(draftId) {
        if (!draftId.isNullOrBlank())
            viewModel.fetchDraft(draftId) {
                title.value = it.name
                description.value = it.description
                price.value = it.price.toString()
                meetingPoint.value = it.meetingPoint
                it.picturesUri?.let { it1 -> pictures.addAll(it1) }
                switch.value = it.type == ListingType.BIDDING
                date.setSelection(it.biddingDeadline.time)
            }
    }

    Screen(
        title = stringResource(R.string.listings_add_title),
        onGoBackClick = navigateBack,
        isLoading = false
    ) {
        ListingForm(
            chosenCategories,
            title = title,
            description = description,
            price = price,
            meetingPoint = meetingPoint,
            pictures = pictures,
            checked = switch,
            onSubmit = {
                viewModel.createListing(
                    title.value,
                    description.value,
                    chosenCategories,
                    price.value.toFloat(),
                    meetingPoint.value,
                    pictures,
                    type = if (switch.value) ListingType.BIDDING else ListingType.FIXED,
                    //Create a date out of the timestamp (it's UTC)
                    deadline =
                    if (date.selectedDateMillis != null) Date(date.selectedDateMillis!!) else Date(),
                    navigateToListing
                )
            },
            onSaveDraft = {
                viewModel.saveDraft(
                    title.value,
                    description.value,
                    chosenCategories,
                    price.value.ifEmpty { "0" }.toFloat(),
                    meetingPoint.value,
                    pictures,
                    type = if (switch.value) ListingType.BIDDING else ListingType.FIXED,
                    //Create a date out of the timestamp (it's UTC)
                    deadline =
                    if (date.selectedDateMillis != null) Date(date.selectedDateMillis!!) else Date()
                ) { displayMessage(context, msg) }
            },

            openCategorySheet = openCategorySheet,
            date = date
        )
        CategorySheet(
            isOpen = openCategorySheet,
            categories = categories,
            chosen = chosenCategories
        )
    }
}
