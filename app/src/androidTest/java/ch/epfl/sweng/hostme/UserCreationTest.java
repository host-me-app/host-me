package ch.epfl.sweng.hostme;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

/*
@RunWith(AndroidJUnit4.class)
public class UserCreationTest {

    @Test
    public void checkFirstNamePage() {
        Intent intent = new Intent(getApplicationContext(), CreationContainer.class);
        try (ActivityScenario<CreationContainer> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.genderNextButton)).check(matches(isDisplayed()));
            onView(withId(R.id.genderNextButton)).perform(click());
            onView(withId(R.id.nextButtonFirstName)).check(matches(isDisplayed()));
            onView(withId(R.id.nextButtonFirstName)).perform(click());
            onView(withId(R.id.nextButtonLastName)).check(matches(isDisplayed()));
        }
    }
}*/
