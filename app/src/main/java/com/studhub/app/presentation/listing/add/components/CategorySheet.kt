package com.studhub.app.presentation.listing.add.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.domain.model.Category
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.text.BigLabel
import com.studhub.app.presentation.ui.theme.StudHubTheme


/**
 * Creates a ModalBottomSheet (material 3) tailored for displaying category choice
 * When the user picks one it removes it from the categories list and adds it to the chosen one
 *
 * @param isOpen takes a mutable boolean to handle sheet apparition on the screen. Otherwise the sheet
 * blocks all interactions, even if hidden.
 * @param categories the list of categories to display in the sheet
 * @param chosen is the list of categories already chosen by the user
 */
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CategorySheet(
    isOpen: MutableState<Boolean>,
    categories: List<Category>,
    chosen: MutableState<MutableList<Category>>
) {
    val sheetState = rememberModalBottomSheetState()
    if (isOpen.value) {
        ModalBottomSheet(
            onDismissRequest = { isOpen.value = false },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            sheetState = sheetState
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                BigLabel("Pick a category ${sheetState.isVisible}")
            }
            Spacer(modifier = Modifier.height(8.dp))
            CategoryItems(categories, chosen)
        }
    }
}

@Composable
fun CategoryItem(name: String, onClick: () -> Unit = { }) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
            headlineColor = MaterialTheme.colorScheme.onSecondaryContainer,
            leadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        headlineContent = { Text(name) },
        leadingContent = {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Localized description",
            )
        },
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun CategoryItems(
    categories: List<Category>,
    chosen: MutableState<MutableList<Category>>
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp)
    ) {
        categories.forEach { cat ->
            CategoryItem(
                name = cat.name,
                onClick = {
                    chosen.value += cat
                    Log.d("CategorySheet", "Selected items are $chosen")
                }
            )
            Divider(color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}

/**
 * This preview does not display well in the design tab but can be played on emulator instead
 */
@SuppressLint("UnrememberedMutableState")
@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun CategorySheetPreview() {
    val categories = List(30) { Category(name = "Cat $it") }
    val openCategorySheet = rememberSaveable { mutableStateOf(false) }
    val chosenCategories = mutableStateOf(mutableListOf<Category>())
    StudHubTheme {
        Box {
            BasicFilledButton(
                onClick = {
                    openCategorySheet.value = true
                    println("Clicked")
                },
                label = "Open"
            )
        }
        if (openCategorySheet.value) {
            CategorySheet(
                openCategorySheet,
                categories,
                chosenCategories
            )
        }
    }
}

