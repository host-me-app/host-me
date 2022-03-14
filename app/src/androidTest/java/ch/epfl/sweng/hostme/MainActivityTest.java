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
import android.widget.Toast;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.base.UiControllerImpl_Factory;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executor;


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
    public void forgotPwdButtonTest() {

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);

        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.forgotPassword)).check(matches(isDisplayed()));
            onView(withId(R.id.forgotPassword)).perform(click());
        }
    }

    @Test
    public void displayWelcome() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "jules.maglione20@gmail.com";
            String password = "!Hostme2022";
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            onView(withId(R.id.userName)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));
            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
            onView(withId(R.id.logInButton)).perform(click());
        }

    }

}
