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

import ch.epfl.sweng.hostme.LogInActivity;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.ui.add.AddFragment;

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
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                ListImage.init(new AddFragment(), ApplicationProvider.getApplicationContext());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }
}
