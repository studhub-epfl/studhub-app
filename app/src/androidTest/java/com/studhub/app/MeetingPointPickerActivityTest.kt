package com.studhub.app


import android.content.Intent
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.MapView
import com.studhub.app.resources.ElapsedTimeIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotSame
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MeetingPointPickerActivityTest {


    @Test
    fun searchLocationAndConfirm() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            MeetingPointPickerActivity::class.java
        )
        intent.putExtra("viewOnly", false)
        intent.putExtra("latitude", 0.0)
        intent.putExtra("longitude", 0.0)
        ActivityScenario.launch<MeetingPointPickerActivity>(intent).use { scenario ->
            // Create idling resource
            val idlingResource2 = ElapsedTimeIdlingResource(10000)
            // Register idling resource
            IdlingRegistry.getInstance().register(idlingResource2)

            var idlingResource: IdlingResource? = null

            var idlingResourceConfirmButton: IdlingResource? = null

            scenario.onActivity { activity ->
                idlingResource = activity.idlingResourceSearchLocation
                IdlingRegistry.getInstance().register(idlingResource)
            }

            // Type in the search view
            onView(allOf(isAssignableFrom(AutoCompleteTextView::class.java)))
                .perform(typeText("New York"), closeSoftKeyboard())

            // Click on the search button
            onView(allOf(isAssignableFrom(Button::class.java), withText("Search")))
                .perform(click())

            // We can't really predict what will be displayed on the map after the search
            // But let's assume that the confirm button will be enabled if the search is successful
            onView(allOf(isAssignableFrom(Button::class.java), withText("Confirm Location")))
                .check(matches(isEnabled()))

            scenario.onActivity { activity ->
                idlingResourceConfirmButton = activity.idlingResourceConfirmButton
                IdlingRegistry.getInstance().register(idlingResourceConfirmButton)
            }

            Thread.sleep(10000)

            scenario.onActivity { activity ->
                IdlingRegistry.getInstance().unregister(idlingResourceConfirmButton)
            }

            // Click on the confirm button
            onView(allOf(isAssignableFrom(Button::class.java), withText("Confirm Location")))
                .perform(click())


//             Wait for the UI to get idle
            IdlingRegistry.getInstance().unregister(idlingResource2)

            Thread.sleep(5000)


            assertNotSame(scenario.state,Lifecycle.State.RESUMED)
        }


    }

    @Test
    fun mapClickAndConfirm() {

        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            MeetingPointPickerActivity::class.java
        )
        intent.putExtra("viewOnly", false)
        intent.putExtra("latitude", 0.0)
        intent.putExtra("longitude", 0.0)

        ActivityScenario.launch<MeetingPointPickerActivity>(intent).use { scenario ->
            var idlingResourceConfirm: IdlingResource? = null

            // Create idling resource
            val idlingResource2 = ElapsedTimeIdlingResource(10000)
            // Register idling resource
            IdlingRegistry.getInstance().register(idlingResource2)


            var idlingResource: IdlingResource? = null

            scenario.onActivity { activity ->
                idlingResource = activity.idlingResourceMapClick
                IdlingRegistry.getInstance().register(idlingResource)
            }

            // Assuming that a click on the map will enable the confirm button
            // Click somewhere on the map
            onView(isAssignableFrom(MapView::class.java)).perform(click())

            scenario.onActivity { activity ->
                idlingResourceConfirm = activity.idlingResourceConfirmButton
                IdlingRegistry.getInstance().register(idlingResourceConfirm)
            }

            Thread.sleep(10000)

            scenario.onActivity { activity ->
                IdlingRegistry.getInstance().unregister(idlingResourceConfirm)
            }

            // Click on the confirm button
            onView(allOf(isAssignableFrom(Button::class.java), withText("Confirm Location")))
                .perform(click())

            // Wait for the UI to get idle
            IdlingRegistry.getInstance().unregister(idlingResource2)


            assertNotSame(scenario.state,Lifecycle.State.RESUMED)

        }  }
}





