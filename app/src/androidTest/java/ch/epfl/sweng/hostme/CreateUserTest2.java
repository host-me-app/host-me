package ch.epfl.sweng.hostme;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import android.content.Intent;
import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class CreateUserTest2 {


    @Test
    public void checkLastNamePage() {
        Intent intent = new Intent(getApplicationContext(), UserCreationPage3.class);
        try (ActivityScenario<UserCreationPage3> scenario = launch(intent)) {
                /*int nextButtonLastName = R.id.nextButtonLastName;
                onView(withId(R.id.lastName)).check(matches(isDisplayed()));
                onView(withId(nextButtonLastName)).perform(click());*/
            onView(withId(R.id.lastName)).perform(clearText()).perform(typeText("Maglione"), closeSoftKeyboard());
            onView(withId(R.id.lastName)).check(matches(isDisplayed()));
            int nextButtonLastName = R.id.nextButtonLastName;
            onView(withId(nextButtonLastName)).check(matches(isDisplayed()));
        }
    }

}
