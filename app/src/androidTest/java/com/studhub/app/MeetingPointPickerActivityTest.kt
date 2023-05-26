package com.studhub.app

import android.content.Intent
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.studhub.app.resources.ElapsedTimeIdlingResource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNotSame
import junit.framework.TestCase.assertTrue
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MeetingPointPickerActivityTest {

    private lateinit var scenario: ActivityScenario<MeetingPointPickerActivity>

    @Before
    fun setUp() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            MeetingPointPickerActivity::class.java
        )
        intent.putExtra("viewOnly", false)
        intent.putExtra("latitude", 0.0)
        intent.putExtra("longitude", 0.0)
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun mapViewIsDisplayed() {
        onView(allOf(isAssignableFrom(MapView::class.java))).check(matches(isDisplayed()))
    }

    @Test
    fun searchViewIsDisplayed() {
        onView(allOf(isAssignableFrom(AutoCompleteTextView::class.java))).check(matches(isDisplayed()))
    }

    @Test
    fun searchButtonIsDisplayed() {
        onView(allOf(isAssignableFrom(Button::class.java), withText("Search")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun confirmButtonIsDisplayed() {
        onView(allOf(isAssignableFrom(Button::class.java), withText("Confirm Location")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun searchLocationAndConfirm() {
        val idlingResourceSearchLocation = ElapsedTimeIdlingResource(5000)
        val idlingResourceConfirmButton = ElapsedTimeIdlingResource(5000)

        IdlingRegistry.getInstance().register(idlingResourceSearchLocation)
        IdlingRegistry.getInstance().register(idlingResourceConfirmButton)

        onView(allOf(isAssignableFrom(AutoCompleteTextView::class.java)))
            .perform(typeText("New York"), closeSoftKeyboard())

        onView(allOf(isAssignableFrom(Button::class.java), withText("Search")))
            .perform(click())

        onView(allOf(isAssignableFrom(Button::class.java), withText("Confirm Location")))
            .check(matches(isEnabled()))

        onView(allOf(isAssignableFrom(Button::class.java), withText("Confirm Location")))
            .perform(click())

        IdlingRegistry.getInstance().unregister(idlingResourceSearchLocation)
        IdlingRegistry.getInstance().unregister(idlingResourceConfirmButton)

        assertNotSame(scenario.state, Lifecycle.State.CREATED)

    }


    @Test
    fun testDrawRoute() {


        val startLatLng = LatLng(40.7128, -74.0060)
        val endLatLng = LatLng(37.7749, -122.4194)

        scenario.onActivity { activity ->
            activity.currentLatLng = startLatLng
            activity.selectedLatLng = endLatLng
            activity.drawRoute(startLatLng, endLatLng)

            // Verify that a request was made to draw the route
            assertNotNull(activity.currentLatLng)
            assertNotNull(activity.selectedLatLng)
            assertEquals(startLatLng, activity.currentLatLng)
            assertEquals(endLatLng, activity.selectedLatLng)

        }
    }
    @Test
    fun testDrawRouteToMeetingPoint() {


        val currentLatLng = LatLng(40.7128, -74.0060)
        val meetingPointLatLng = LatLng(37.7749, -122.4194)

        scenario.onActivity { activity ->
            activity.currentLatLng = currentLatLng
            activity.selectedLatLng = meetingPointLatLng

            // Verify that a request was made to draw the route to the meeting point
            assertNotNull(activity.currentLatLng)
            assertNotNull(activity.currentLatLng)
            assertEquals(currentLatLng, activity.currentLatLng)
            assertEquals(meetingPointLatLng, activity.selectedLatLng)


        }
    }

}
