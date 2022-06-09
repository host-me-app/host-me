package ch.epfl.sweng.hostme.maps;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.activities.LogInActivity;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

public class MapsTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void clickOnMapsTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());

            onView(withId(R.id.search_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.maps_button)).check(matches(isDisplayed()));
            onView(withId(R.id.maps_button)).check(matches(isEnabled()));
            onView(withId(R.id.maps_button)).perform(click());
            onView(isRoot()).perform(ViewActions.pressBack());
            onView(withId(R.id.maps_button)).perform(click());
        }
        Intents.release();
    }

    @Test
    public void clickOnDailyRouteTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());

            onView(withId(R.id.search_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.maps_button)).perform(click());
            onView(withId(R.id.daily_route_maps)).check(matches(isDisplayed()));
            onView(withId(R.id.daily_route_maps)).check(matches(isEnabled()));
            onView(withId(R.id.daily_route_maps)).perform(click());
        }
        Intents.release();
    }

    @Test
    public void clickOnStreetViewTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());

            onView(withId(R.id.search_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.street_view_button)).check(matches(isDisplayed()));
            onView(withId(R.id.street_view_button)).check(matches(isEnabled()));
            onView(withId(R.id.street_view_button)).perform(click());
            onView(isRoot()).perform(ViewActions.pressBack());
            onView(withId(R.id.street_view_button)).perform(click());
        }
        Intents.release();
    }

}