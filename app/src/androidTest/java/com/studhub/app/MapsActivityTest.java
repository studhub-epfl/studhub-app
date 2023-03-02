package com.studhub.app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.not;

import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.Toast;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RunWith(AndroidJUnit4.class)
public class MapsActivityTest {

    @Rule
    public ActivityScenarioRule<MapsActivity> activityScenarioRule = new ActivityScenarioRule<>(MapsActivity.class);

    @Test
    public void testMapViewIsDisplayed() {
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    // Couldn't figure out how to make this test work
    @Test
    public void testZoomInOnMap() {
        onView(withId(R.id.map)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(MapView.class);
            }

            @Override
            public String getDescription() {
                return "Zooming in on map";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((MapView) view).getMapAsync(googleMap -> {
                    googleMap.moveCamera(CameraUpdateFactory.zoomIn());
                });
            }
        });
    }

    // Couldn't figure out how to make this test work
    @Test
    public void testClickOnMarkerDisplaysAddress() {
        onView(allOf(isAssignableFrom(MapView.class), withId(R.id.map))).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(MapView.class);
            }

            @Override
            public String getDescription() {
                return "Clicking on marker";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((MapView) view).getMapAsync(googleMap -> {
                    LatLng satellite = new LatLng(46.520544, 6.567825);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(satellite)
                            .title("Satellite")
                            .alpha(0.8f);
                    Marker marker = googleMap.addMarker(markerOptions);
                    marker.setTag(satellite);

                    Geocoder geocoder = new Geocoder(view.getContext(), Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses = geocoder.getFromLocation(satellite.latitude, satellite.longitude, 1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String address = addresses.get(0).getAddressLine(0);

                    googleMap.setOnMarkerClickListener(marker1 -> {
                        LatLng position = (LatLng) marker1.getTag();
                        if (position != null) {
                            Toast.makeText(view.getContext(), "Marker clicked at: " + address, Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    });
                });
            }
        });

        // Use onActivity() here to get the activity instance
        activityScenarioRule.getScenario().onActivity(activity -> {
            onView(withText("Satellite")).perform(click());
            onView(withText("Rue de la Maladière 71, 1022 Chavannes-près-Renens, Switzerland"))
                    .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                    .check(matches(isDisplayed()));
        });
    }
}

