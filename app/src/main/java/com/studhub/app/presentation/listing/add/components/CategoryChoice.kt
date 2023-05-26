package com.studhub.app.presentation.listing.add.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studhub.app.domain.model.Category
import com.studhub.app.presentation.ui.common.button.PlusButton
import com.studhub.app.presentation.ui.common.text.TextChip


/**
 * This is the category picker part. There is a modal drawer that works as a category picker and
 * the chosen categories are displayed in "Chip" composables. They can be clicked off to be
 * removed from the chosen list
 * @param chosen the list state of chosen categories
 * @param openCategorySheet a boolean state to decide whether the modal drawer is opened or closed.
 */
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
