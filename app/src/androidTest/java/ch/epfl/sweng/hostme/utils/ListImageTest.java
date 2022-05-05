package ch.epfl.sweng.hostme.utils;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.GrantPermissionRule;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.MainActivity;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;

public class ListImageTest {
    @Rule
    public GrantPermissionRule internetAccess = GrantPermissionRule.grant(android.Manifest.permission.INTERNET);
    public GrantPermissionRule readAccess = GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE);

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void initializeTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String usr = "testlogin@gmail.com";
            String pwd = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(usr), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());

            String path = "a/p_b_r";
            scenario.onActivity(activity -> {
                ListImage.init(path, activity, ApplicationProvider.getApplicationContext());
                assertEquals(path, ListImage.getPath());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }
}