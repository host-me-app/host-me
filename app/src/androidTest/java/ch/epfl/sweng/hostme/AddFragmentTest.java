package ch.epfl.sweng.hostme;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.MainActivity;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility;

@RunWith(AndroidJUnit4.class)
public class AddFragmentTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void initialStateTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String usr = "testlogin@gmail.com";
            String pwd = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(usr), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());

            onView(withId(R.id.navigation_add)).perform(click());

            onView(withId(R.id.add_new)).check(matches(isDisplayed()));
            onView(withId(R.id.add_new)).check(matches(isClickable()));
            onView(withId(R.id.add_form)).check(matches(withEffectiveVisibility(Visibility.GONE)));
            onView(withId(R.id.add_buttons)).check(matches(withEffectiveVisibility(Visibility.GONE)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void openFormTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String usr = "testlogin@gmail.com";
            String pwd = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(usr), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());

            onView(withId(R.id.navigation_add)).perform(click());

            onView(withId(R.id.add_new)).perform((click()));

            onView(withId(R.id.add_form)).check(matches(isDisplayed()));
            onView(withId(R.id.add_buttons)).check(matches(isDisplayed()));
            onView(withId(R.id.enter_images)).check(matches(isNotClickable()));
            onView(withId(R.id.add_submit)).check(matches(isNotClickable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void incompleteFormTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String usr = "testlogin@gmail.com";
            String pwd = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(usr), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());

            onView(withId(R.id.navigation_add)).perform(click());

            onView(withId(R.id.add_new)).perform((click()));

            onView(withId(R.id.enter_proprietor)).perform(typeText("a"), closeSoftKeyboard());
            onView(withId(R.id.enter_name)).perform(typeText("b"), closeSoftKeyboard());
            onView(withId(R.id.enter_room)).perform(typeText("c"), closeSoftKeyboard());
            onView(withId(R.id.enter_address)).perform(typeText("d"), closeSoftKeyboard());
            onView(withId(R.id.enter_npa)).perform(typeText("1"), closeSoftKeyboard());
            onView(withId(R.id.enter_city)).perform(typeText("e"), closeSoftKeyboard());
            onView(withId(R.id.enter_rent)).perform(typeText("2"), closeSoftKeyboard());
            onView(withId(R.id.enter_utilities)).perform(typeText("3"), closeSoftKeyboard());
            onView(withId(R.id.enter_deposit)).perform(typeText("4"), closeSoftKeyboard());
            onView(withId(R.id.enter_beds)).perform(typeText("5"), closeSoftKeyboard());
            onView(withId(R.id.enter_area)).perform(typeText("6"), closeSoftKeyboard());
            onView(withId(R.id.enter_duration)).perform(typeText("7"), closeSoftKeyboard());

            onView(withId(R.id.enter_images)).check(matches(isNotClickable()));
            onView(withId(R.id.add_submit)).check(matches(isNotClickable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void generateApartmentTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String usr = "testlogin@gmail.com";
            String pwd = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(usr), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());

            onView(withId(R.id.navigation_add)).perform(click());

            onView(withId(R.id.add_new)).perform((click()));

            onView(withId(R.id.enter_proprietor)).perform(typeText("a"), closeSoftKeyboard());
            onView(withId(R.id.enter_name)).perform(typeText("b"), closeSoftKeyboard());
            onView(withId(R.id.enter_room)).perform(typeText("c"), closeSoftKeyboard());
            onView(withId(R.id.enter_address)).perform(typeText("d"), closeSoftKeyboard());
            onView(withId(R.id.enter_npa)).perform(typeText("1"), closeSoftKeyboard());
            onView(withId(R.id.enter_city)).perform(typeText("e"), closeSoftKeyboard());
            onView(withId(R.id.enter_rent)).perform(typeText("2"), closeSoftKeyboard());
            onView(withId(R.id.enter_utilities)).perform(typeText("3"), closeSoftKeyboard());
            onView(withId(R.id.enter_deposit)).perform(typeText("4"), closeSoftKeyboard());
            onView(withId(R.id.enter_beds)).perform(typeText("5"), closeSoftKeyboard());
            onView(withId(R.id.enter_area)).perform(typeText("6"), closeSoftKeyboard());
            onView(withId(R.id.enter_duration)).perform(typeText("7"), closeSoftKeyboard());

            onView(withId(R.id.pets_yes)).perform(click());
            onView(withId(R.id.enter_images)).check(matches(isClickable()));

            onView(withId(R.id.enter_images)).perform(click());
            onView(withId(R.id.add_submit)).check(matches(isClickable()));

            onView(withId(R.id.add_submit)).perform(click());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

}
