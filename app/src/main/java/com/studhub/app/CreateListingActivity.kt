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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.repository.CategoryRepositoryImpl
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.ListingRepository
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.button.PlusButton
import com.studhub.app.presentation.ui.common.input.BasicTextField
import com.studhub.app.presentation.ui.common.input.TextBox
import com.studhub.app.presentation.ui.common.text.BigLabel
import com.studhub.app.ui.theme.StudHubTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CreateListingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateListingView()
        }
    }
}

private val categoriesRepository: CategoryRepositoryImpl = CategoryRepositoryImpl()

private val repository: ListingRepository = object : ListingRepository {

    private val listingDB = HashMap<String, Listing>()

    override suspend fun createListing(listing: Listing): Flow<ApiResponse<Listing>> {
        return flow {
            emit(ApiResponse.Loading)
            delay(1000)
            listingDB[listing.id] = listing
            emit(ApiResponse.Success(listing))
        }
    }

    override suspend fun getListings(): Flow<ApiResponse<List<Listing>>> {
        // empty implementation
        return flowOf(ApiResponse.Loading)
    }

    override suspend fun getListing(listingId: String): Flow<ApiResponse<Listing>> {
        // empty implementation
        return flowOf(ApiResponse.Loading)
    }

    override suspend fun updateListing(
        listingId: String,
        updatedListing: Listing
    ): Flow<ApiResponse<Listing>> {
        // empty implementation
        return flowOf(ApiResponse.Loading)
    }

    override suspend fun removeListing(listingId: String): Flow<ApiResponse<Boolean>> {
        // empty implementation
        return flowOf(ApiResponse.Loading)
    }
}

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

@Composable
fun CreateListingView() {
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

    BasicTextField(label = "Item title")
    AddImageLayout(onClick = {})
    TextBox(label = "Item description")
    PriceRow()
    CategoryDropDown()
    BasicFilledButton(
        onClick = {
            scope.launch {

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
fun PriceRow() {
    var price by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.width(TextFieldDefaults.MinWidth),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .width(100.dp)
                .padding(end = 4.dp),
            singleLine = true,
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text("CHF")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDown() {
    var categories: List<Category>
    runBlocking {
        categories = getCategoriesList()
    }
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(categories[0]) }

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
                value = selectedText.name,
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
                            selectedText = cat
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
