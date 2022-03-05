package ch.epfl.sweng.hostme;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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
public class CreateUserTest {

    @Rule
    public ActivityScenarioRule<UserCreationPage1> rule =
            new ActivityScenarioRule<>(UserCreationPage1.class);

    @Test
    public void checkGenderPage() {
        Intent intent = new Intent(getApplicationContext(), UserCreationPage1.class);
        try (ActivityScenario<UserCreationPage1> scenario = launch(intent)) {
            onView(withId(R.id.nextButtonGender)).perform(click());
        }
    }

    @Test
    public void checkFirstNamePage() {
        Intent intent = new Intent(getApplicationContext(), UserCreationPage2.class);
        try (ActivityScenario<UserCreationPage2> scenario = launch(intent)){
            int nextButtonFirstName = R.id.nextButtonFirstName;
            onView(withId(nextButtonFirstName)).perform(click());
        }
    }

    @Test
    public void checkLastNamePage() {
        Intent intent = new Intent(getApplicationContext(), UserCreationPage3.class);
        try (ActivityScenario<UserCreationPage3> scenario = launch(intent)) {
            int nextButtonLastName = R.id.nextButtonLastName;
            onView(withId(nextButtonLastName)).perform(click());
        }
    }

    @Test
    public void checkMailPage() {
        Intent intent = new Intent(getApplicationContext(), UserCreationPage4.class);
        try(ActivityScenario<UserCreationPage4> scenario = launch(intent)) {
            int nextButtonMail = R.id.nextButtonMail;
            onView(withId(nextButtonMail)).perform(click());
        }
    }

    @Test
    public void checkPwdPage() {
        Intent intent = new Intent(getApplicationContext(), UserCreationPage5.class);
        try (ActivityScenario<UserCreationPage5> scenario = launch(intent)){
            int terminateButton = R.id.terminateButton;
            onView(withId(terminateButton)).check(matches(isDisplayed()));
            onView(withId(terminateButton)).perform(click());
        }
    }

}
