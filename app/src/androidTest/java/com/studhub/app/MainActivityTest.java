package com.studhub.app;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import static org.hamcrest.core.AllOf.allOf;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;


import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void intentIsCreatedWhenNameFormIsSubmitted() {
        String key = "name";
        String value = "Bob";

        Intents.init();

        onView(withId(R.id.mainName))
                .perform(typeText(value))
                .perform(closeSoftKeyboard());

        onView(withId(R.id.mainGoButton))
                .perform(click());

        intended(allOf(
            hasExtra(key, value),
            hasComponent(GreetActivity.class.getName())));

        Intents.release();
    }
}
