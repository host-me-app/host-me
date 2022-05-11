package ch.epfl.sweng.hostme.wallet;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.widget.DatePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.google.firebase.FirebaseApp;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.hostme.LogInActivity;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;


@RunWith(AndroidJUnit4.class)
public class ExpirationDatePickingTest {

    @Rule
    public GrantPermissionRule internetRule = GrantPermissionRule.grant(android.Manifest.permission.INTERNET);

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }



    @Test
    public void expirationDatePickingTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).perform(click());
            Thread.sleep(1000);

            /*onView(withId(R.id.wallet_button)).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.buttonPickDate_ResidencePermit)).perform(click());
            Thread.sleep(1000);
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                    .perform(PickerActions.setDate(2022, 3, 22));
            Thread.sleep(1000);
            onView(withId(android.R.id.button1)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.textExpirationDate_ResidencePermit)).check(matches(allOf(withText("22/3/2022"),
                    isDisplayed())));

            Thread.sleep(5000);*/

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }
}