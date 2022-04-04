package ch.epfl.sweng.hostme;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class WelcomePageTest {

    @Test
    public void welcomeMessageIsDisplayed() {
        Intent intent = new Intent(getApplicationContext(), WelcomePage.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = launch(intent)) {
            onView(withId(R.id.welcomeMessage)).check(matches(isDisplayed()));
        }
        Intents.release();
    }
}