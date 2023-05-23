package com.studhub.app

import android.content.Intent
import android.widget.AutoCompleteTextView
import android.widget.Button
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
import com.studhub.app.resources.ElapsedTimeIdlingResource
import junit.framework.TestCase.assertNotSame
import junit.framework.TestCase.assertSame
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

        assertNotSame(scenario.state,Lifecycle.State.CREATED)

    }

    @Test
    fun mapClickAndConfirm() {
        val idlingResourceMapClick = ElapsedTimeIdlingResource(5000)
        val idlingResourceConfirmButton = ElapsedTimeIdlingResource(5000)

        IdlingRegistry.getInstance().register(idlingResourceMapClick)
        IdlingRegistry.getInstance().register(idlingResourceConfirmButton)

        onView(isAssignableFrom(MapView::class.java)).perform(click())

        onView(allOf(isAssignableFrom(Button::class.java), withText("Confirm Location")))
            .check(matches(isEnabled()))

        onView(allOf(isAssignableFrom(Button::class.java), withText("Confirm Location")))
            .perform(click())

        IdlingRegistry.getInstance().unregister(idlingResourceMapClick)
        IdlingRegistry.getInstance().unregister(idlingResourceConfirmButton)

        assertNotSame(scenario.state, Lifecycle.State.CREATED)

    }

    @Test
    fun viewOnlyModeOpenInGoogleMaps() {
        val viewOnlyIntent = Intent(
            ApplicationProvider.getApplicationContext(),
            MeetingPointPickerActivity::class.java
        ).apply {
            putExtra("viewOnly", true)
            putExtra("latitude", 0.0)
            putExtra("longitude", 0.0)
        }


        scenario.close()

        scenario = ActivityScenario.launch(viewOnlyIntent)

        onView(allOf(isAssignableFrom(Button::class.java), withText("Open in Google Maps")))
            .check(matches(isDisplayed()))

        onView(allOf(isAssignableFrom(Button::class.java), withText("Open in Google Maps")))
            .perform(click())

        assertNotSame(scenario.state, Lifecycle.State.CREATED)

    }
}
