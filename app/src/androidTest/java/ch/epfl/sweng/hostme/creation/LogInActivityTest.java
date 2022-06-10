package ch.epfl.sweng.hostme.creation;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.activities.LogInActivity;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;


@RunWith(AndroidJUnit4.class)
public class LogInActivityTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void checkLoginWithGoodValues() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String email = "testlogin@gmail.com";
            String pwd = "fakePassword1!";
            onView(ViewMatchers.withId(R.id.user_name)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));

            onView(withId(R.id.user_name)).perform(clearText()).perform(typeText(email), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(clearText()).perform(typeText(pwd), closeSoftKeyboard());

            onView(withId(R.id.user_name)).check(matches(withText(email)));
            onView(withId(R.id.pwd)).check(matches(withText(pwd)));

            onView(withId(R.id.log_in_button)).check(matches(isDisplayed()));
            onView(withId(R.id.log_in_button)).check(matches(isEnabled()));
            onView(withId(R.id.log_in_button)).perform(click());
        }
        Intents.release();
    }

    @Test
    public void checkLoginWithFalseValues() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String email = "testlogin@gmail.com";
            String pwd = "fakepassword";
            onView(withId(R.id.user_name)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));

            onView(withId(R.id.user_name)).perform(clearText()).perform(typeText(email), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(clearText()).perform(typeText(pwd), closeSoftKeyboard());

            onView(withId(R.id.user_name)).check(matches(withText(email)));
            onView(withId(R.id.pwd)).check(matches(withText(pwd)));

            onView(withId(R.id.log_in_button)).check(matches(isDisplayed()));
            onView(withId(R.id.log_in_button)).perform(click());
        }
        Intents.release();
    }

    @Test
    public void checkSignUpButton() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.sign_up_button)).check(matches(isDisplayed()));
            onView(withId(R.id.sign_up_button)).check(matches(isEnabled()));
            onView(withId(R.id.sign_up_button)).perform(click());
        }
        Intents.release();
    }

    @Test
    public void forgotPwdButtonTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.forgot_password)).check(matches(isDisplayed()));
            onView(withId(R.id.forgot_password)).check(matches(isEnabled()));
            onView(withId(R.id.forgot_password)).perform(click());
        }
        Intents.release();
    }

}
