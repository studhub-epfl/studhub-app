package com.studhub.app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.studhub.app.database.MockDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FirebaseActivityTest {
    @Test
    public void retrievedValueIsIdenticalToSetValue() {
        Globals.DATABASE = new MockDatabase();

        String email = "john.doe@epfl.ch";
        String phone = "0791234567";

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), FirebaseActivity.class);

        try (ActivityScenario<FirebaseActivity> activity = ActivityScenario.launch(intent)) {
            // write email to email input
            onView(withId(R.id.firebase_input_email))
                    .perform(typeText(email))
                    .perform(closeSoftKeyboard());

            // write phone to phone input
            onView(withId(R.id.firebase_input_phone))
                    .perform(typeText(phone))
                    .perform(closeSoftKeyboard());

            // click the SET button
            onView(withId(R.id.firebase_btn_set)).perform(click());

            // clear email and phone fields
            onView(withId(R.id.firebase_input_email)).perform(clearText());
            onView(withId(R.id.firebase_input_phone)).perform(clearText());

            // click the GET button
            onView(withId(R.id.firebase_btn_get)).perform(click());

            // check retrieved value in email field
            onView(withId(R.id.firebase_input_email)).check(matches(withText(email)));

            // check retrieved value in phone field
            onView(withId(R.id.firebase_input_phone)).check(matches(withText(phone)));
        }
    }
}
