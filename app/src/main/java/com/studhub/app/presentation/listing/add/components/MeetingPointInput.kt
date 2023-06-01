package com.studhub.app.presentation.listing.add.components

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.LatLng
import com.studhub.app.MeetingPointPickerActivity
import com.studhub.app.domain.model.MeetingPoint
import com.studhub.app.presentation.ui.common.button.BasicFilledButton


@Composable
fun MeetingPointInput(meetingPoint: MutableState<MeetingPoint?>) {
    val context = LocalContext.current
    val requestLocationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val location = data?.getParcelableExtra<LatLng>("location")
            location?.let { position ->
                meetingPoint.value =
                    MeetingPoint(latitude = position.latitude, longitude = position.longitude)
            }
        }
    }

    BasicFilledButton(
        label = "Set Meeting Point",
        onClick = {
            val intent = Intent(context, MeetingPointPickerActivity::class.java)
            requestLocationLauncher.launch(intent)
        }
    )

}
