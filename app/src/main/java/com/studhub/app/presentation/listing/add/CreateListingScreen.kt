package com.studhub.app.presentation.listing.add

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.R
import com.studhub.app.domain.model.Category
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.button.PlusButton
import com.studhub.app.presentation.ui.common.input.BasicTextField
import com.studhub.app.presentation.ui.common.input.TextBox
import com.studhub.app.presentation.ui.common.text.BigLabel
import com.studhub.app.presentation.ui.theme.StudHubTheme

@Composable
fun CreateListingScreen(
    viewModel: CreateListingViewModel = hiltViewModel(),
    navigateToListing: (id: String) -> Unit
) {
    val categories by viewModel.categories.collectAsState(emptyList())

    val title = rememberSaveable { mutableStateOf("") }
    val description = rememberSaveable { mutableStateOf("") }
    val price = rememberSaveable { mutableStateOf("") }
    val category = remember { mutableStateOf(Category(name = "Choose a category")) }

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
                    categories,
                    title = title,
                    description = description,
                    price = price,
                    category = category,
                    onSubmit = {
                        viewModel.createListing(
                            title.value,
                            description.value,
                            category.value,
                            price.value.toFloat(),
                            navigateToListing
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ListingForm(
    categories: List<Category>,
    title: MutableState<String>,
    description: MutableState<String>,
    price: MutableState<String>,
    category: MutableState<Category>,
    onSubmit: () -> Unit,
) {

    BasicTextField(label = "Item title", rememberedValue = title)
    AddImageLayout(onClick = {})
    TextBox(label = "Item description", rememberedValue = description)
    PriceRow(rememberedValue = price)
    CategoryDropDown(categories, selected = category)
    BasicFilledButton(
        onClick = {
            if (category.value.name != "Choose a category") {
                onSubmit()
            }
        },
        label = "Create"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalMaterial3Api::class)
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
                focusedTextColor = MaterialTheme.colorScheme.onBackground),
            singleLine = true,
            value = rememberedValue.value,
            onValueChange = { rememberedValue.value = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text("CHF")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDown(
    categories: List<Category>,
    selected: MutableState<Category> = rememberSaveable { mutableStateOf(Category(name = "Choose a category")) }
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {

            ListingSelectedCategoryField(
                modifier = Modifier
                    .menuAnchor()
                    .width(TextFieldDefaults.MinWidth),
                value = selected.value.name,
                expanded = expanded)

            //Would like to have that block in a separate composable but ExposedDropdownMenu
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                categories.forEach { cat ->
                    ListingDropDownItem (
                        label = cat.name,
                        onClick = {
                            selected.value = cat
                            expanded = false
                        })
                }
            }
        }
    }
}

@Composable
fun ListingDropDownItem(label: String, onClick: () -> Unit = {}) {
    DropdownMenuItem(
        text = {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSecondaryContainer)
        },
        onClick = onClick,
        colors = MenuDefaults.itemColors()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingSelectedCategoryField(modifier: Modifier, value: String, expanded: Boolean) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer),
        modifier = modifier
    )
}
