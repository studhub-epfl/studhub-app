package com.studhub.app.presentation.ratings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User

@Composable
fun UserHeader(
    targetUser: State<ApiResponse<User>>,
    thumbsUpCount: Int,
    thumbsDownCount: Int
) {
    when (val userResponse = targetUser.value) {
        is ApiResponse.Loading -> {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp, start = 16.dp))
        }
        is ApiResponse.Success -> {
            Text(
                text = userResponse.data.firstName + " " + userResponse.data.lastName,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text("Thumbs Up: $thumbsUpCount", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Thumbs Down: $thumbsDownCount", fontSize = 20.sp
                )
            }
        }
        else -> {
            Text("Error", modifier = Modifier.padding(top = 16.dp, start = 16.dp))
        }
    }
}
