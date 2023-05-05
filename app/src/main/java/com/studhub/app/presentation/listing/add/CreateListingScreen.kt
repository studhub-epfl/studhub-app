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
    val category = remember { mutableStateOf(Category(name = "Choose a category")) }
    val pictures = rememberSaveable { mutableListOf<Uri>() }


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
                    openCategorySheet,
                    chosenCategories,
                    title = title,
                    description = description,
                    price = price,
                    category = category,
                    pictures = pictures,
                    onSubmit = {
                        viewModel.createListing(
                            title.value,
                            description.value,
                            chosenCategories.value[0],
                            price.value.toFloat(),
                            pictures,
                            navigateToListing
                        )
                    }
                )
            }
        }
    )
}

@Composable
fun ListingForm(
    openCategorySheet: MutableState<Boolean>,
    chosen: MutableState<MutableList<Category>>,
    title: MutableState<String>,
    description: MutableState<String>,
    price: MutableState<String>,
    category: MutableState<Category>,
    pictures: MutableList<Uri>,
    onSubmit: () -> Unit,
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

    PriceRow(rememberedValue = price)

    Spacer("large")

    Spacer("large")

    val categoryInputDefaultName = stringResource(R.string.listings_add_form_category)
    BasicFilledButton(
        onClick = {
            if (category.value.name != categoryInputDefaultName) {
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
    chosen : MutableState<MutableList<Category>>,
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
        chosen.value.forEach() { cat ->
            Text(cat.name)
        }
    }
}

