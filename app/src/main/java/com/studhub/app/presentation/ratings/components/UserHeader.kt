package com.studhub.app.presentation.ratings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.ui.common.icons.CustomThumbsDown
import com.studhub.app.presentation.ui.common.icons.CustomThumbsUp

@Composable
fun UserHeader(
    targetUser: State<ApiResponse<User>>,
    thumbsUpCount: Int,
    thumbsDownCount: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (val userResponse = targetUser.value) {
            is ApiResponse.Loading -> {
                Box(Modifier.testTag("loading")) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp, start = 16.dp))
                }
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
                    Image(
                        painter = CustomThumbsUp(),
                        contentDescription = "Thumbs Up",
                        modifier = Modifier.size(width = 36.dp, height = 36.dp)
                    )
                    Text(" $thumbsUpCount", fontSize = 20.sp, modifier = Modifier.testTag("ThumbsUpCount"))
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = CustomThumbsDown(), contentDescription = "Thumbs Down",
                        modifier = Modifier.size(width = 36.dp, height = 36.dp)
                    )
                    Text(" $thumbsDownCount", fontSize = 20.sp, modifier = Modifier.testTag("ThumbsDownCount"))
                }
            }

            else -> {
                Text("Error", modifier = Modifier.padding(top = 16.dp, start = 16.dp))
            }
        }
    }
}
