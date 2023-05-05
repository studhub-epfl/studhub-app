package com.studhub.app.presentation.listing.add

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
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
import com.studhub.app.presentation.ui.common.button.PlusButton
import com.studhub.app.presentation.ui.common.input.BasicTextField
import com.studhub.app.presentation.ui.common.input.TextBox
import com.studhub.app.presentation.ui.common.text.BigLabel

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun CreateListingScreen(
    viewModel: CreateListingViewModel = hiltViewModel(),
    navigateToListing: (id: String) -> Unit
) {
    val categories by viewModel.categories.collectAsState(emptyList())

    val title = rememberSaveable { mutableStateOf("") }
    val description = rememberSaveable { mutableStateOf("") }
    val price = rememberSaveable { mutableStateOf("") }
    val chosenCategories = rememberSaveable { mutableStateOf(mutableListOf<Category>()) }
    val openCategorySheet = rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BigLabel(label = stringResource(R.string.listings_add_title))
                ListingForm(
                    openCategorySheet,
                    chosenCategories,
                    title = title,
                    description = description,
                    price = price,
                    onSubmit = {
                        viewModel.createListing(
                            title.value,
                            description.value,
                            chosenCategories.value[0],
                            price.value.toFloat(),
                            navigateToListing
                        )
                    }
                )
            }
        }
    }
    CategorySheet(
        isOpen = openCategorySheet,
        categories = categories,
        chosen = chosenCategories
    )
}

@Composable
fun ListingForm(
    openCategorySheet: MutableState<Boolean>,
    chosen: MutableState<MutableList<Category>>,
    title: MutableState<String>,
    description: MutableState<String>,
    price: MutableState<String>,
    onSubmit: () -> Unit,
) {

    BasicTextField(label = "Item title", rememberedValue = title)
    AddImageLayout(onClick = {})
    TextBox(label = "Item description", rememberedValue = description)
    PriceRow(rememberedValue = price)
    CategoryChoice(chosen, openCategorySheet)
    BasicFilledButton(
        onClick = {
            if (chosen.value.isNotEmpty()) {
                onSubmit()
            }
        },
        label = "Create"
    )
}

@Composable
fun AddImageLayout(onClick: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = TextFieldDefaults.MinHeight
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlusButton(onClick = { isVisible = true })
        if (!isVisible) {
            Text("No images yet")
        }
    }
    if (isVisible) {
        ImageCarousel()
    }
}


@Composable
fun ImageCarousel() {
    Box(
        modifier = Modifier
            .width(TextFieldDefaults.MinWidth)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary))
    ) {
        Image(
            imageVector = Icons.Filled.AccountBox,
            modifier = Modifier.aspectRatio(1f),
            contentDescription = "placeholder"
        )
    }
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
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text("CHF")
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

