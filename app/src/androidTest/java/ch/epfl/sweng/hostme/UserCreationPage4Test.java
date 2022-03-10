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
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class UserCreationPage4Test {

    @Rule
    public ActivityScenarioRule<UserCreationPage4> testRule = new ActivityScenarioRule<>(UserCreationPage4.class);

    @Test
    public void checkMailPage() {
        Intents.init();

        onView(withId(R.id.nextButtonMail)).perform(click());
        onView(withId(R.id.terminateButton)).check(matches(isDisplayed()));

        Intents.release();
    }

}


