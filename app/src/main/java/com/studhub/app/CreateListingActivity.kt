package com.studhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.repository.CategoryRepositoryImpl
import com.studhub.app.data.repository.ListingRepositoryImpl
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.domain.usecase.listing.CreateListing
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.button.PlusButton
import com.studhub.app.presentation.ui.common.input.BasicTextField
import com.studhub.app.presentation.ui.common.input.TextBox
import com.studhub.app.presentation.ui.common.text.BigLabel
import com.studhub.app.ui.theme.StudHubTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

class CreateListingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateListingView()
        }
    }
}

private val categoriesRepository = CategoryRepositoryImpl()

private val listingRepository = ListingRepositoryImpl()

private var categories = emptyList<Category>()

suspend fun getCategoriesList(): List<Category> {
    val getCategories = GetCategories(categoriesRepository)
    var retrievedCategories: List<Category> = emptyList()
    getCategories().collect {
        when (it) {
            is ApiResponse.Success -> retrievedCategories = it.data
            is ApiResponse.Failure -> {/* should not fail */
            }
            is ApiResponse.Loading -> {}
        }
    }
    return retrievedCategories
}

suspend fun createListing(listing: Listing) {
    val createListing = CreateListing(listingRepository)
    createListing(listing).collect {
        when (it) {
            is ApiResponse.Success -> {/* TODO success message and/or return to another view */
            }
            is ApiResponse.Failure -> {/* should not fail */
            }
            is ApiResponse.Loading -> {/* TODO SHOW LOADING ICON */
            }
        }
    }
}

@Composable
fun CreateListingView() {
    LaunchedEffect(categories) {
        categories = getCategoriesList()
    }
    StudHubTheme {
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
                    BigLabel(label = "Create Listing")
                    ListingForm()
                }
            }
        }
    }
}

@Composable
fun ListingForm() {
    val scope = rememberCoroutineScope()
    val title = rememberSaveable { mutableStateOf("") }
    val description = rememberSaveable { mutableStateOf("") }
    val price = rememberSaveable { mutableStateOf("") }
    val category = remember { mutableStateOf(Category(name = "Choose a category")) }

    BasicTextField(label = "Item title", rememberedValue = title)
    AddImageLayout(onClick = {})
    TextBox(label = "Item description", rememberedValue = description)
    PriceRow(rememberedValue = price)
    CategoryDropDown(selected = category)
    BasicFilledButton(
        onClick = {
            val listing = Listing(
                id = Random.nextInt().toString(),
                seller = User(userName = "Placeholder Name"),
                name = title.value,
                description = description.value,
                categories = listOf(category.value),
                price = price.value.toFloat()
            )
            if (category.value.name != "Choose a category") {
                println("Crated listing")
                scope.launch {
                    createListing(listing)
                }
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
            OutlinedTextField(
                value = selected.value.name,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .width(TextFieldDefaults.MinWidth)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                categories.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(text = cat.name) },
                        onClick = {
                            selected.value = cat
                            expanded = false
                        },
                        colors = MenuDefaults.itemColors()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateListingPreview() {
    CreateListingView()
}
