package com.studhub.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

class MapsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val epfl = LatLng(46.520536, 6.568318)
            val satellite = LatLng(46.520544, 6.567825)

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(epfl, 15f)
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = satellite),
                    title = "Satellite",
                    tag = satellite,
                    onInfoWindowClick = { marker ->
                        Toast.makeText(
                            this@MapsActivity,
                            "Marker clicked at ${marker.position}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }

}
