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

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class UserCreationPage3Test {

    @Rule
    public ActivityScenarioRule<UserCreationPage3> rule =
            new ActivityScenarioRule<UserCreationPage3>(UserCreationPage3.class);

    @Test
    public void checkLastNamePage() {
        Intents.init();

        onView(withId(R.id.lastName)).perform(clearText()).perform(typeText("Maglione"), closeSoftKeyboard());
        onView(withId(R.id.lastName)).check(matches(isDisplayed()));
        onView(withId(R.id.nextButtonLastName)).perform(click());

        Intents.release();
    }
}


