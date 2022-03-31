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
import androidx.test.espresso.intent.Intents;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;

public class loginTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void checkLoginWithValues() throws Exception {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String username = "testlogin@gmail.com";
            String pwd = "fakePassword1!";
            onView(withId(R.id.userName)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));

            onView(withId(R.id.userName)).perform(clearText()).perform(typeText(username), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(clearText()).perform(typeText(pwd), closeSoftKeyboard());

            onView(withId(R.id.userName)).check(matches(withText(username)));
            onView(withId(R.id.pwd)).check(matches(withText(pwd)));

            onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
            onView(withId(R.id.logInButton)).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.nav_host_fragment_activity_menu1)).check(matches(isDisplayed()));
        }
        Intents.release();
    }
}
