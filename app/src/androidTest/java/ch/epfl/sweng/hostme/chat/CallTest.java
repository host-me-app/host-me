package ch.epfl.sweng.hostme.chat;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.Manifest;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.ui.messages.UsersActivity;

@RunWith(AndroidJUnit4.class)
public class CallTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.BLUETOOTH_CONNECT
    );


    @Test
    public void callUser() {
        Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
        Intents.init();
        try (ActivityScenario<UsersActivity> scenario = ActivityScenario.launch(intent)) {
            Thread.sleep(2000);
            onView(withId(R.id.usersRecyclerView)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, click()));

            onView(withId(R.id.launchButt)).perform(click());
            onView(withId(R.id.audioBtn)).perform(click());
            onView(withId(R.id.audioBtn)).perform(click());
            onView(withId(R.id.audioBtn)).perform(click());
            onView(withId(R.id.videoBtn)).perform(click());
            onView(withId(R.id.videoBtn)).perform(click());
            onView(withId(R.id.switch_camera)).perform(click());
            onView(withId(R.id.switch_camera)).perform(click());
            Thread.sleep(500);
            onView(withId(R.id.leaveBtn)).perform(click());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }
}