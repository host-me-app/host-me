package ch.epfl.sweng.hostme;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class UserCreationPage4Test {

    @Test
    public void checkMailPage() {

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserCreationPage4.class);

        try (ActivityScenario<UserCreationPage4> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.nextButtonMail)).check(matches(isDisplayed()));
            onView(withId(R.id.nextButtonMail)).perform(click());
            onView(withId(R.id.terminateButton)).check(matches(isDisplayed()));
        }
    }

}


