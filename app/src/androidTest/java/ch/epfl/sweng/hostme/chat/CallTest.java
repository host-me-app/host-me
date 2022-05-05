package ch.epfl.sweng.hostme.chat;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.Manifest;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.hostme.MenuActivity;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

@RunWith(AndroidJUnit4.class)
public class CallTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(getApplicationContext());
    }

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    );


    @Test
    public void callUser() {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        Intents.init();
        try (ActivityScenario<MenuActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.navigation_messages)).perform(click());
            onView(withId(R.id.contactButton)).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.usersRecyclerView)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, click()));

            onView(withId(R.id.launchButt)).perform(click());
            onView(withId(R.id.audioBtn)).perform(click());
            onView(withId(R.id.videoBtn)).perform(click());
            onView(withId(R.id.leaveBtn)).perform(click());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }
}
