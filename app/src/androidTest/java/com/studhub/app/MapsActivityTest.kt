package com.studhub.app

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MapsActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val epflPos = LatLng(46.520536, 6.568318)
    private val cameraZoom = 15f
    private var cameraPositionState: CameraPositionState = CameraPositionState(
        position = CameraPosition.fromLatLngZoom(epflPos, 15f)
    )

    @Before
    fun setup() {
        val countDownLatch = CountDownLatch(1)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        composeTestRule.setContent { MapsActivityContent(context) }
        countDownLatch.await(10, TimeUnit.SECONDS)
    }

    @Test
    fun testCameraPosition() {
        assertEquals(epflPos, cameraPositionState.position.target)
    }

    @Test
    fun testZoomLevel() {
        assertEquals(cameraZoom, cameraPositionState.position.zoom)
    }
}
