package ch.epfl.sweng.hostme.userCreation;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;


@RunWith(AndroidJUnit4.class)
public class UserCreationTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void checkGenderPageMale() {
        Intent intent = new Intent(getApplicationContext(), CreationContainer.class);
        Intents.init();
        try (ActivityScenario<CreationContainer> scenario = ActivityScenario.launch(intent)) {
            onView(ViewMatchers.withId(R.id.genderNextButton)).check(matches(isDisplayed()));
            onView(withId(R.id.genderNextButton)).perform(click());
            onView(withId(R.id.nextButtonFirstName)).check(matches(isDisplayed()));
        }
        Intents.release();
    }

    @Test
    public void createGoodUser() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CreationContainer.class);
        Intents.init();
        try (ActivityScenario<CreationContainer> scenario = ActivityScenario.launch(intent)) {

            String email = "host.me.test2022@gmail.com";
            String pwd = "fakePassword1!";

            onView(withId(R.id.radioF)).check(matches(isDisplayed()));
            onView(withId(R.id.radioF)).perform(click());
            onView(withId(R.id.genderNextButton)).check(matches(isDisplayed()));
            onView(withId(R.id.genderNextButton)).perform(click());

            onView(withId(R.id.nextButtonFirstName)).check(matches(isDisplayed()));
            onView(withId(R.id.nextButtonFirstName)).perform(click());

            onView(withId(R.id.nextButtonLastName)).check(matches(isDisplayed()));
            onView(withId(R.id.nextButtonLastName)).perform(click());

            onView(withId(R.id.nextButtonSchool)).perform(click());

            onView(withId(R.id.mail)).perform(typeText(email), closeSoftKeyboard());
            onView(withId(R.id.nextButtonMail)).check(matches(isDisplayed()));
            onView(withId(R.id.nextButtonMail)).perform(click());

            onView(withId(R.id.password)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.confirm_pwd)).perform(typeText(pwd), closeSoftKeyboard());

            onView(withId(R.id.terminateButton)).check(matches(isDisplayed()));
            onView(withId(R.id.terminateButton)).perform(click());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void createBadUser() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CreationContainer.class);
        Intents.init();
        try (ActivityScenario<CreationContainer> scenario = ActivityScenario.launch(intent)) {

            String email = "host.me.test2022@gmail.com";
            String pwd = "fakePassword1!";

            onView(withId(R.id.genderNextButton)).perform(click());
            onView(withId(R.id.nextButtonFirstName)).perform(click());
            onView(withId(R.id.nextButtonLastName)).perform(click());
            onView(withId(R.id.nextButtonSchool)).perform(click());


            onView(withId(R.id.mail)).perform(typeText(email), closeSoftKeyboard());
            onView(withId(R.id.nextButtonMail)).perform(click());

            onView(withId(R.id.password)).perform(typeText("!"), closeSoftKeyboard());
            onView(withId(R.id.confirm_pwd)).perform(typeText("!Hostme"), closeSoftKeyboard());
            onView(withId(R.id.terminateButton)).perform(click());
            onView(withId(R.id.password)).check(matches(hasErrorText("The password must be " +
                    "at least 8 characters and contains at least 1 uppercase character and 1 special character")));

            onView(withId(R.id.password)).perform(typeText("Hostme"), closeSoftKeyboard());
            onView(withId(R.id.terminateButton)).perform(click());
            onView(withId(R.id.password)).perform(typeText("2022"), closeSoftKeyboard());
            onView(withId(R.id.terminateButton)).perform(click());
            onView(withId(R.id.confirm_pwd)).check(matches(hasErrorText("The passwords should be identical")));
            onView(withId(R.id.confirm_pwd)).perform(typeText("2022"), closeSoftKeyboard());

            onView(withId(R.id.terminateButton)).check(matches(isDisplayed()));
            onView(withId(R.id.terminateButton)).perform(click());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void createAndGobackButton() {
        Intent intent = new Intent(getApplicationContext(), CreationContainer.class);
        Intents.init();
        try (ActivityScenario<CreationContainer> scenario = ActivityScenario.launch(intent)) {
            String email = "host.me.test2022@gmail.com";
            String pwd = "fakePassword1!";

            UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            onView(withId(R.id.genderNextButton)).perform(click());
            mDevice.pressBack();
            onView(withId(R.id.genderNextButton)).perform(click());
            onView(withId(R.id.nextButtonFirstName)).perform(click());
            mDevice.pressBack();
            onView(withId(R.id.nextButtonFirstName)).perform(click());
            onView(withId(R.id.nextButtonLastName)).perform(click());
            mDevice.pressBack();
            onView(withId(R.id.nextButtonLastName)).perform(click());
            onView(withId(R.id.nextButtonSchool)).perform(click());
            mDevice.pressBack();
            onView(withId(R.id.nextButtonSchool)).perform(click());
            onView(withId(R.id.mail)).perform(typeText(email), closeSoftKeyboard());
            onView(withId(R.id.nextButtonMail)).perform(click());
            mDevice.pressBack();
            onView(withId(R.id.mail)).perform(typeText(email), closeSoftKeyboard());
            mDevice.pressBack();
        }
        Intents.release();
    }

    @Test
    public void wrongMail() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CreationContainer.class);
        Intents.init();
        try (ActivityScenario<CreationContainer> scenario = ActivityScenario.launch(intent)) {
            String email = "host";
            onView(withId(R.id.genderNextButton)).perform(click());
            onView(withId(R.id.nextButtonFirstName)).perform(click());
            onView(withId(R.id.nextButtonLastName)).perform(click());
            onView(withId(R.id.nextButtonSchool)).perform(click());

            onView(withId(R.id.mail)).perform(typeText(email), closeSoftKeyboard());
            onView(withId(R.id.mail)).check(matches(hasErrorText("You should enter a valid mail address")));

        }
        Intents.release();
    }


}
