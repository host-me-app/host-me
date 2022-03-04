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

import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void checkLoginWithValues() {
        String username = "nonexistinguser@example.com";
        String pwd = "invalidpwd";
        onView(withId(R.id.userName)).check(matches(isDisplayed()));
        onView(withId(R.id.pwd)).check(matches(isDisplayed()));

        onView(withId(R.id.userName)).perform(clearText()).perform(typeText("nonexistinguser@example.com"), closeSoftKeyboard());
        onView(withId(R.id.pwd)).perform(clearText()).perform(typeText("invalidpwd"), closeSoftKeyboard());

        onView(withId(R.id.logInButton)).check(matches(isDisplayed()));

        onView(withId(R.id.userName)).check(matches(withText(username)));
        onView(withId(R.id.pwd)).check(matches(withText(pwd)));

        onView(withId(R.id.logInButton)).perform(click());

    }

    @Test
    public void checkSignUpButton() {
        onView(withId(R.id.signUpButton)).check(matches(isDisplayed()));
        onView(withId(R.id.signUpButton)).perform(click());
    }

    @Test
    public void ForgotPwdButtonTest() {
        onView(withId(R.id.passwordForgot)).check(matches(isDisplayed()));
    }



}
