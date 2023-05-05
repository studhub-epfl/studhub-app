/**
 * @author Francesc Vilarino Guell
 * @author Studhub Team
 *
 * @see <a href="https://fvilarino.medium.com/using-activity-result-contracts-in-jetpack-compose-14b179fb87de">Medium article</a>
 * @see <a href="https://developer.android.com/reference/androidx/core/content/FileProvider">Android Doc - FileProvider</a>
 *
 */

package com.studhub.app.presentation.ui.common.input

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.studhub.app.R
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import java.io.File

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
    onNewPicture: (Uri) -> Unit,
    displayPickedImage: Boolean = false
) {
    var hasImage by remember {
        mutableStateOf(false)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            hasImage = uri != null
            imageUri = uri
            handleNewPicture(imageUri, hasImage, onNewPicture)
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
            handleNewPicture(imageUri, success, onNewPicture)
        }
    )

    val context = LocalContext.current

    Box(
        modifier = modifier,
    ) {
        if (displayPickedImage) {
            if (hasImage && imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    modifier = Modifier
                        .fillMaxWidth(0.5F)
                        .align(Alignment.Center),
                    contentDescription = "Selected image",
                )
            }
        }

        Row(
            modifier = Modifier.padding(bottom = 32.dp),
        ) {
            BasicFilledButton(
                label = "Select image",
                onClick = {
                    imagePicker.launch("image/*")
                }
            )

            BasicFilledButton(
                label = "Take a photo",
                onClick = {
                    val uri = ComposeFileProvider.getImageUri(context)
                    imageUri = uri
                    cameraLauncher.launch(uri)
                }
            )
        }
    }
}

private fun handleNewPicture(uri: Uri?, success: Boolean, onSuccess: (Uri) -> Unit) {
    if (success && uri != null) {
        onSuccess(uri)
    }
}

/**
 *
 */
class ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory,
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}
