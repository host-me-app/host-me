package ch.epfl.sweng.hostme.creation;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.activities.ChangePwdHomeActivity;
import ch.epfl.sweng.hostme.activities.LogInActivity;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;


@RunWith(AndroidJUnit4.class)
public class ChangePwdTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void enterWrongMail() {
        Intent intent = new Intent(getApplicationContext(), ChangePwdHomeActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = launch(intent)) {
            String wrongMail = "host.me@gmail.com";
            onView(withId(R.id.mail_forgot_pwd)).check(matches(isDisplayed()));
            onView(withId(R.id.mail_forgot_pwd)).perform(typeText(wrongMail), closeSoftKeyboard());
            onView(withId(R.id.next_button_mail)).check(matches(isDisplayed()));
            onView(withId(R.id.next_button_mail)).check(matches(isEnabled()));
            onView(withId(R.id.next_button_mail)).perform(click());
        }
        Intents.release();
    }

    @Test
    public void enterValidMail() {
        Intent intent = new Intent(getApplicationContext(), ChangePwdHomeActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = launch(intent)) {
            String validMail = "testlogin@gmail.com";
            onView(withId(R.id.mail_forgot_pwd)).perform(typeText(validMail), closeSoftKeyboard());
            onView(withId(R.id.next_button_mail)).check(matches(isDisplayed()));
            onView(withId(R.id.next_button_mail)).check(matches(isDisplayed()));
            onView(withId(R.id.next_button_mail)).check(matches(isEnabled()));
            onView(withId(R.id.next_button_mail)).perform(click());
        }
        Intents.release();
    }
}