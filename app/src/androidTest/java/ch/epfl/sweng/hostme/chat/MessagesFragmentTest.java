package ch.epfl.sweng.hostme.chat;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.FirebaseApp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.hostme.MenuActivity;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

@RunWith(AndroidJUnit4.class)
public class MessagesFragmentTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @AfterClass
    public static void after_class()
    {
        Intents.release();
    }

    @Test
    public void CheckContactButton() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MenuActivity.class);
        Intents.init();
        try (ActivityScenario<MenuActivity> scenario = ActivityScenario.launch(intent)) {

            onView(withId(R.id.navigation_messages)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.contactButton)).check(matches(isDisplayed()));
            onView(withId(R.id.contactButton)).perform(click());
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void CheckImageProfileDisplayed() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MenuActivity.class);
        Intents.init();
        try (ActivityScenario<MenuActivity> scenario = ActivityScenario.launch(intent)) {

            onView(withId(R.id.navigation_messages)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.imageProfileChat)).check(matches(isDisplayed()));
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void CheckTextDisplayed() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MenuActivity.class);
        Intents.init();
        try (ActivityScenario<MenuActivity> scenario = ActivityScenario.launch(intent)) {

            String m = "Messages";
            onView(withId(R.id.navigation_messages)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.textHome)).check(matches(isDisplayed()));
            onView(withId(R.id.textHome)).check(matches(withText(m)));
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void CheckRecentConvDisplayed() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MenuActivity.class);
        Intents.init();
        try (ActivityScenario<MenuActivity> scenario = ActivityScenario.launch(intent)) {

            String m = "Messages";
            onView(withId(R.id.navigation_messages)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.FrameConv)).check(matches(isDisplayed()));
            onView(withId(R.id.conversationRecycler)).check(matches(isDisplayed()));
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }
}
