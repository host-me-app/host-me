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
    public void checkFirstNamePage() {
        Intent intent = new Intent(getApplicationContext(), UserCreationPage2.class);
        try (ActivityScenario<UserCreationPage2> scenario = launch(intent)) {
            onView(withId(R.id.firstName)).perform(clearText()).perform(typeText("Jules"), closeSoftKeyboard());
            onView(withId(R.id.firstName)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void checkLastNamePage() {
        Intent intent = new Intent(getApplicationContext(), UserCreationPage3.class);
        try (ActivityScenario<UserCreationPage3> scenario = launch(intent)) {
            onView(withId(R.id.lastName)).perform(clearText()).perform(typeText("Maglione"), closeSoftKeyboard());
            onView(withId(R.id.lastName)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void checkMailPage() {
        Intent intent = new Intent(getApplicationContext(), UserCreationPage4.class);
        try (ActivityScenario<UserCreationPage4> scenario = launch(intent)) {
            onView(withId(R.id.mail)).perform(clearText()).perform(typeText("jules.aozozoz@epfl.ch"), closeSoftKeyboard());
            onView(withId(R.id.mail)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void checkPwdPage() {
        Intent intent = new Intent(getApplicationContext(), UserCreationPage5.class);
        try (ActivityScenario<UserCreationPage5> scenario = launch(intent)) {
            onView(withId(R.id.password)).perform(clearText()).perform(typeText("rightPwd"), closeSoftKeyboard());
            onView(withId(R.id.password)).check(matches(isDisplayed()));
            onView(withId(R.id.confirm_pwd)).perform(clearText()).perform(typeText("rightPwd"), closeSoftKeyboard());
            onView(withId(R.id.confirm_pwd)).check(matches(isDisplayed()));
        }
    }


}
