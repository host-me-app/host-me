package ch.epfl.sweng.hostme;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.not;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class UserCreationTest {

    @Test
    public void checkGenderPageMale() {
        Intent intent = new Intent(getApplicationContext(), CreationContainer.class);
        try (ActivityScenario<CreationContainer> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.genderNextButton)).check(matches(isDisplayed()));
            onView(withId(R.id.genderNextButton)).perform(click());
            onView(withId(R.id.nextButtonFirstName)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void checkGenderPageFemale() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CreationContainer.class);
        try (ActivityScenario<CreationContainer> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.radioF)).check(matches(isDisplayed()));
            onView(withId(R.id.radioF)).perform(click());
            onView(withId(R.id.genderNextButton)).check(matches(isDisplayed()));
            onView(withId(R.id.genderNextButton)).perform(click());
            onView(withId(R.id.nextButtonFirstName)).check(matches(isDisplayed()));

            onView(withId(R.id.nextButtonFirstName)).check(matches(isDisplayed()));
            onView(withId(R.id.nextButtonFirstName)).perform(click());
            onView(withId(R.id.nextButtonLastName)).check(matches(isDisplayed()));

            onView(withId(R.id.nextButtonLastName)).check(matches(isDisplayed()));
            onView(withId(R.id.nextButtonLastName)).perform(click());
            onView(withId(R.id.nextButtonMail)).check(matches(isDisplayed()));


            onView(withId(R.id.mail)).perform(typeText("nadalPol"), closeSoftKeyboard());
            onView(withId(R.id.nextButtonMail)).check(matches(not(isEnabled())));

            onView(withId(R.id.mail)).perform(clearText()).perform(typeText("jules.maglione@epfl.ch"), closeSoftKeyboard());
            onView(withId(R.id.nextButtonMail)).check(matches(isDisplayed()));
            onView(withId(R.id.nextButtonMail)).perform(click());
            onView(withId(R.id.terminateButton)).check(matches(isDisplayed()));


            onView(withId(R.id.password)).check(matches(isDisplayed()));
            onView(withId(R.id.confirm_pwd)).check(matches(isDisplayed()));
            onView(withId(R.id.password)).perform(typeText("!Hostme2022"), closeSoftKeyboard());
            onView(withId(R.id.confirm_pwd)).perform(typeText("!Hostme2022"), closeSoftKeyboard());

            onView(withId(R.id.terminateButton)).check(matches(isDisplayed()));
            onView(withId(R.id.terminateButton)).perform(click());
        }
    }


    @Test
    public void checkFirstNamePage() {

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CreationContainer.class);

        try (ActivityScenario<CreationContainer> scenario = ActivityScenario.launch(intent)) {
        }
    }

    @Test
    public void checkLastNamePage() {

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CreationContainer.class);

        try (ActivityScenario<CreationContainer> scenario = ActivityScenario.launch(intent)) {
        }
    }

    @Test
    public void checkMailPage() {

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CreationContainer.class);

        try (ActivityScenario<CreationContainer> scenario = ActivityScenario.launch(intent)) {
        }
    }

    @Test
    public void checkPwdPage() {

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CreationContainer.class);

        try (ActivityScenario<CreationContainer> scenario = ActivityScenario.launch(intent)) {
        }
    }

}
