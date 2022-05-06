package ch.epfl.sweng.hostme.userCreation;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
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

import ch.epfl.sweng.hostme.LogInActivity;
import ch.epfl.sweng.hostme.R;
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
        Intent intent = new Intent(getApplicationContext(), EnterMailChangePwd.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = launch(intent)) {
            String wrongMail = "host.me@gmail.com";
            onView(withId(R.id.mailForgotPwd)).perform(typeText(wrongMail), closeSoftKeyboard());
            onView(withId(R.id.nextButtonMail2)).check(matches(isDisplayed()));
            onView(withId(R.id.nextButtonMail2)).perform(click());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void enterValidMail() {
        Intent intent = new Intent(getApplicationContext(), EnterMailChangePwd.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = launch(intent)) {
            String validMail = "testlogin@gmail.com";
            onView(withId(R.id.mailForgotPwd)).perform(typeText(validMail), closeSoftKeyboard());
            onView(withId(R.id.nextButtonMail2)).check(matches(isDisplayed()));
            onView(withId(R.id.nextButtonMail2)).perform(click());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }
}