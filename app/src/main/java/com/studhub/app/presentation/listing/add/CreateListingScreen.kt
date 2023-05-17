package com.studhub.app.presentation.listing.add

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.LatLng
import com.studhub.app.MeetingPointPickerActivity
import com.studhub.app.R
import com.studhub.app.core.utils.PriceValidationResult
import com.studhub.app.core.utils.validatePrice
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.MeetingPoint
import com.studhub.app.presentation.listing.add.components.CategorySheet
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.button.PlusButton
import com.studhub.app.presentation.ui.common.container.Carousel
import com.studhub.app.presentation.ui.common.input.BasicTextField
import com.studhub.app.presentation.ui.common.input.ImagePicker
import com.studhub.app.presentation.ui.common.input.TextBox
import com.studhub.app.presentation.ui.common.text.BigLabel
import com.studhub.app.presentation.ui.common.misc.Spacer
import com.studhub.app.presentation.ui.common.text.TextChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListingScreen(
    viewModel: CreateListingViewModel = hiltViewModel(),
    navigateToListing: (id: String) -> Unit,
    navigateBack: () -> Unit
) {
    val categories by viewModel.categories.collectAsState(emptyList())
    val title = rememberSaveable { mutableStateOf("") }
    val description = rememberSaveable { mutableStateOf("") }
    val price = rememberSaveable { mutableStateOf("") }
    val category = remember { mutableStateOf(Category(name = "Choose a category")) }
    val meetingPoint = remember { mutableStateOf<MeetingPoint?>(null) }
    val pictures = rememberSaveable { mutableListOf<Uri>() }
    val chosenCategories = remember { mutableStateListOf<Category>() }
    val openCategorySheet = rememberSaveable { mutableStateOf(false) }


    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.listings_add_title)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.misc_btn_go_back),
                        )
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(scrollState)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ListingForm(
                    chosenCategories,
                    title = title,
                    description = description,
                    price = price,
                    category = category,
                    meetingPoint = meetingPoint,
                    pictures = pictures,
                    onSubmit = {
                        viewModel.createListing(
                            title.value,
                            description.value,
                            chosenCategories,
                            price.value.toFloat(),
                            meetingPoint.value,
                            pictures,
                            navigateToListing
                        )
                    },
                    openCategorySheet = openCategorySheet
                )
            }
            CategorySheet(
                isOpen = openCategorySheet,
                categories = categories,
                chosen = chosenCategories
            )
        }
    )
}

@Composable
fun ListingForm(
    chosen: SnapshotStateList<Category>,
    title: MutableState<String>,
    description: MutableState<String>,
    price: MutableState<String>,

    category: MutableState<Category>,
    meetingPoint: MutableState<MeetingPoint?>,
    pictures: MutableList<Uri>,
    onSubmit: () -> Unit,
    openCategorySheet: MutableState<Boolean>
) {
    BasicTextField(
        label = stringResource(R.string.listings_add_form_title),
        rememberedValue = title
    )

    Spacer("large")

    if (pictures.isNotEmpty()) {
        Carousel(modifier = Modifier.fillMaxWidth(0.8F), pictures = pictures)
    }

    ImagePicker(onNewPicture = { pictures.add(it) })

    Spacer("large")

    TextBox(
        label = stringResource(R.string.listings_add_form_description),
        rememberedValue = description
    )

    Spacer("large")

    CategoryChoice(chosen = chosen, openCategorySheet = openCategorySheet)

    Spacer("large")


    PriceRow(rememberedValue = price)

    Spacer("large")
    MeetingPointInput(meetingPoint = meetingPoint)

    Spacer("large")


    val priceValidationResult = validatePrice(price.value)

    if (priceValidationResult != PriceValidationResult.VALID) {
        Text(
            text = when (priceValidationResult) {
                PriceValidationResult.NEGATIVE -> stringResource(R.string.listing_add_form_validation_neg_price)
                PriceValidationResult.NON_NUMERIC -> stringResource(R.string.listing_add_form_validation_numeric_price)
                else -> ""
            },
            modifier = Modifier.padding(4.dp),
            color = Color.Red
        )
    }

    // Check if the category is selected and the price is non-negative
    val isFormValid =  priceValidationResult == PriceValidationResult.VALID
    Button(
        onClick = {
            if (isFormValid) {
                onSubmit()
            }
        },
        enabled = isFormValid,
        modifier = Modifier.padding(top = 3.dp, bottom = 3.dp)
    ) {
        Text(stringResource(R.string.listings_add_form_send))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRow(rememberedValue: MutableState<String> = rememberSaveable { mutableStateOf("") }) {
    Row(
        modifier = Modifier.width(TextFieldDefaults.MinWidth),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .width(100.dp)
                .padding(end = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            singleLine = true,
            value = rememberedValue.value,
            onValueChange = { rememberedValue.value = it },
            label = { Text(stringResource(R.string.listings_add_form_price)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(stringResource(R.string.misc_currency_symbol))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChoice(
    chosen: SnapshotStateList<Category>,
    openCategorySheet: MutableState<Boolean>
) {
    Row(modifier = Modifier.width(TextFieldDefaults.MinWidth)) {
        Box(modifier = Modifier.padding(top = 12.dp)) {
            Text(text = "Add a category:")
        }
        Spacer(modifier = Modifier.width(6.dp))
        PlusButton { openCategorySheet.value = true }
    }
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .width(TextFieldDefaults.MinWidth)
    ) {
        chosen.forEach() { cat ->
            TextChip(
                onClick = { chosen.remove(cat) },
                label = cat.name,
                trailingIcon = {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "Remove ${cat.name}",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            )
        }
    }
}



@Composable
fun MeetingPointInput(meetingPoint: MutableState<MeetingPoint?>) {
    val context = LocalContext.current
    val requestLocationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val location = data?.getParcelableExtra<LatLng>("location")
            location?.let { position ->
                meetingPoint.value =
                    MeetingPoint(latitude = position.latitude, longitude = position.longitude)
            }
        }
    }

    Button(
        onClick = {
            val intent = Intent(context, MeetingPointPickerActivity::class.java)
            requestLocationLauncher.launch(intent)
        }
    ) {
        Text("Set Meeting Point")
    }

}

