package com.studhub.app.presentation.ui.common.misc

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.studhub.app.R


/**
 * A simple rounded container for pictures
 *
 * @param picture the URL of the picture to display - can be a [String] or [Uri]
 */
@Composable
fun <T> Avatar(picture: T) {
    Image(
        painter = if (picture != null) rememberAsyncImagePainter(picture) else painterResource(id = R.drawable.ic_launcher_foreground_red),
        contentDescription = "Avatar",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
    )
}

@Preview(showBackground = true)
@Composable
fun AvatarPreview() {
    Avatar(picture = null)
}
