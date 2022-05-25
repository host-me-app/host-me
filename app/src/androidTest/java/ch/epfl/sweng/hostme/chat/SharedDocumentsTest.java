package ch.epfl.sweng.hostme.chat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.hostme.LogInActivity;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.ui.messages.UsersActivity;

@RunWith(AndroidJUnit4.class)
public class SharedDocumentsTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void ShareDocuments() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<UsersActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());

            onView(withId(R.id.navigation_messages)).perform(click());
            onView(withId(R.id.contactButton)).perform(click());
            onView(withId(R.id.usersRecyclerView))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            Thread.sleep(1000);

            UiDevice device = UiDevice.getInstance(getInstrumentation());

            onView(withId(R.id.shareButton)).perform(click());
            UiObject cancel = device.findObject(new UiSelector().text("CANCEL"));
            cancel.click();
            onView(withId(R.id.shareButton)).perform(click());
            UiObject pick_residence = device.findObject(new UiSelector().text("Residence permit"));
            pick_residence.click();
            UiObject pick_extract = device.findObject(new UiSelector().text("Extract from the Execution Office"));
            pick_extract.click();
            UiObject confirm = device.findObject(new UiSelector().text("SHARE"));
            confirm.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }
}
