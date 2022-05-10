package ch.epfl.sweng.hostme;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.Manifest;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    );


    @Test
    public void callUser() {
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            UiDevice device = UiDevice.getInstance(getInstrumentation());
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.recyclerView)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, click()));
            Thread.sleep(1000);
            onView(withId(R.id.contact_user_button)).perform(click());

            /*onView(withId(R.id.launchButt)).perform(click());
            Thread.sleep(1000);
            UiObject allowPermissions = device.findObject(new UiSelector().text("Allow"));
            allowPermissions.click();
*/
            /*Thread.sleep(1000);
            onView(withId(R.id.audioBtn)).perform(click());
            onView(withId(R.id.audioBtn)).perform(click());
            onView(withId(R.id.audioBtn)).perform(click());
            onView(withId(R.id.videoBtn)).perform(click());
            onView(withId(R.id.videoBtn)).perform(click());
            onView(withId(R.id.switch_camera)).perform(click());
            onView(withId(R.id.switch_camera)).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.leaveBtn)).perform(click());*/
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

}