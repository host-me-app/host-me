package ch.epfl.sweng.hostme;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Test
    public void checkLoginWithValues() {

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);

        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String username = "nonexistinguser@example.com";
            String pwd = "invalidpwd";
            onView(withId(R.id.userName)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));

            onView(withId(R.id.userName)).perform(clearText()).perform(typeText("nonexistinguser@example.com"), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(clearText()).perform(typeText("invalidpwd"), closeSoftKeyboard());

            onView(withId(R.id.userName)).check(matches(withText(username)));
            onView(withId(R.id.pwd)).check(matches(withText(pwd)));

            onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
            onView(withId(R.id.logInButton)).perform(click());
        }
    }

    @Test
    public void checkSignUpButton() {

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);

        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.signUpButton)).check(matches(isDisplayed()));
            onView(withId(R.id.signUpButton)).perform(click());
        }
    }

    @Test
    public void ForgotPwdButtonTest() {

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);

        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.forgotPassword)).check(matches(isDisplayed()));
            onView(withId(R.id.forgotPassword)).perform(click());
        }
    }

}