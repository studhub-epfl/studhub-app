package com.studhub.app.presentation.ratings.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.ratings.components.AddRatingButton
import com.studhub.app.presentation.ratings.components.RatingItem
import com.studhub.app.presentation.ratings.components.RatingsList
import com.studhub.app.presentation.ratings.components.UserHeader
import java.util.*
@Composable
fun AddRatingButton(onShowDialogToggle: () -> Unit,
                    currentUser: State<ApiResponse<User>>,
                    currentUserLoading: State<Boolean>,
                    modifier: Modifier = Modifier) {
    TextButton(
        onClick = {
            onShowDialogToggle()
        },
        colors = ButtonDefaults.textButtonColors(contentColor = Color.DarkGray),
        enabled = currentUser.value is ApiResponse.Success && !currentUserLoading.value,
        modifier = modifier
    ) {
        Text("Add Rating", fontSize = 14.sp)
    }
}
