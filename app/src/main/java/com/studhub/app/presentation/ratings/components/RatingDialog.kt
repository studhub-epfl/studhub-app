package com.studhub.app.presentation.ratings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User

@Composable
fun RatingDialog(
    showDialog: Boolean,
    setShowDialog: () -> Unit,
    onSubmit: (Boolean, String) -> Unit,
    thumbUp: Boolean,
    setThumbUp: (Boolean) -> Unit,
    ratingText: String,
    setRatingText: (String) -> Unit,
    currentUser: State<ApiResponse<User>>,
    currentUserLoading: State<Boolean>
) {
    if (showDialog) {
        Dialog(onDismissRequest = { setShowDialog() }) {
            Surface(modifier = Modifier.size(300.dp, 200.dp)) {
                Column {
                    Text("Thumbs Up / Thumbs Down")
                    Row {
                        Checkbox(checked = thumbUp, onCheckedChange = setThumbUp)
                        Checkbox(checked = !thumbUp, onCheckedChange = { setThumbUp(!it) })
                    }
                    TextField(
                        value = ratingText,
                        onValueChange = setRatingText,
                        modifier = Modifier.weight(1f).fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            onSubmit(thumbUp, ratingText)
                            setShowDialog()
                        },
                        content = { Text("Submit") },
                        enabled = currentUser.value is ApiResponse.Success && !currentUserLoading.value
                    )
                }
            }
        }
    }
}
