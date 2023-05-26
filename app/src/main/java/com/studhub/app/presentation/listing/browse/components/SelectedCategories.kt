package com.studhub.app.presentation.listing.browse.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studhub.app.domain.model.Category
import com.studhub.app.presentation.ui.common.text.TextChip

@Composable
fun SelectedCategories(
    categories: SnapshotStateList<Category>,
    onSelect: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .width(TextFieldDefaults.MinWidth)
    ) {
        categories.forEach() { cat ->
            TextChip(
                onClick = {
                    categories.remove(cat)
                    onSelect()
                },
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
