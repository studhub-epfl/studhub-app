package com.studhub.app.presentation.listing.add

import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.R
import com.studhub.app.domain.model.Category
import com.studhub.app.presentation.listing.add.components.CategorySheet
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.button.PlusButton
import com.studhub.app.presentation.ui.common.container.Carousel
import com.studhub.app.presentation.ui.common.input.BasicTextField
import com.studhub.app.presentation.ui.common.input.ImagePicker
import com.studhub.app.presentation.ui.common.input.TextBox
import com.studhub.app.presentation.ui.common.misc.Spacer

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
    val pictures = rememberSaveable { mutableListOf<Uri>() }
    val chosenCategories = rememberSaveable { mutableStateListOf<Category>() }
    val openCategorySheet = rememberSaveable { mutableStateOf(false)}


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
                    pictures = pictures,
                    onSubmit = {
                        viewModel.createListing(
                            title.value,
                            description.value,
                            chosenCategories[0],
                            price.value.toFloat(),
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
                chosen = chosenCategories)
        }
    )
}

@Composable
fun ListingForm(
    chosen: SnapshotStateList<Category>,
    title: MutableState<String>,
    description: MutableState<String>,
    price: MutableState<String>,
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

    Spacer("large")

    BasicFilledButton(
        onClick = {
            if (chosen.isNotEmpty()) {
                onSubmit()
            }
        },
        label = stringResource(R.string.listings_add_form_send)
    )
}

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

@Composable
fun CategoryChoice(
    chosen : SnapshotStateList<Category>,
    openCategorySheet: MutableState<Boolean>) {
    Log.d("CategoryChoice", "Loaded")
    Row {
        Text(text = "Chose categories:")
        Spacer(modifier = Modifier.width(6.dp))
        PlusButton { openCategorySheet.value = true }
    }
    Column(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        chosen.forEach() { cat ->
            Text(cat.name)
        }
    }
}

