package ch.epfl.sweng.hostme.chat;

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
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
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
public class InfoActivityTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void chatInfoButtonsDisplayed() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.search_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.contact_user_button)).perform(click());

            onView(withId(R.id.chat_info)).check(matches(isDisplayed()));
            onView(withId(R.id.chat_info)).check(matches(isEnabled()));
            onView(withId(R.id.chat_info)).perform(click());

            onView(withId(R.id.grade_button_info)).check(matches(isDisplayed()));
            onView(withId(R.id.grade_button_info)).check(matches(isEnabled()));
            onView(withId(R.id.grade_button_info)).perform(click());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }
}
