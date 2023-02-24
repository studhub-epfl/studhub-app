package com.studhub.app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class GreetActivityTest {

    @Test
    public void correctGreetingIsDisplayed() {
        String key = "name";
        String value = "Bob";
        String message = String.format("Hello my dear %s, how is you?", value);

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), GreetActivity.class);
        intent.putExtra(key, value);

        try (ActivityScenario<GreetActivity> activity = ActivityScenario.launch(intent)) {
            onView(withId(R.id.greetingMessage)).check(matches(withText(message)));
        }
    }
}
