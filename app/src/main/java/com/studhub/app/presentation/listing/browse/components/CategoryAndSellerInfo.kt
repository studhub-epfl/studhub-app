package com.studhub.app.presentation.listing.browse.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.User

@Composable
fun CategoryAndSellerInfo(
    category: Category,
    seller: User,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .wrapContentSize(Alignment.BottomEnd)
    ) {
        Column {
            Text(
                text = "Category: " + category.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
