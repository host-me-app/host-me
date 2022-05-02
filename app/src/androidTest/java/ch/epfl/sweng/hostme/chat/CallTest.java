package ch.epfl.sweng.hostme.chat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.Manifest;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.GrantPermissionRule;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.hostme.MainActivity;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

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
            Manifest.permission.RECORD_AUDIO
            /*Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.CAMERA,
            Manifest.permission.BLUETOOTH*/
    );


    @Test
    public void callUser() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());
            onView(withId(R.id.navigation_messages)).perform(click());
            onView(withId(R.id.contactButton)).perform(click());
            Thread.sleep(1000);
            /*onView(withId(R.id.usersRecyclerView)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, click()));

            onView(withId(R.id.launchButt)).perform(click());
            onView(withId(R.id.joinBtn)).perform(click());
            onView(withId(R.id.audioBtn)).perform(click());
            onView(withId(R.id.videoBtn)).perform(click());
            onView(withId(R.id.leaveBtn)).perform(click());*/

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }
}