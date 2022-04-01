package ch.epfl.sweng.hostme;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MenuActivityTest {

    @Test
    public void SearchFragOpen_whenIconClicked() {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        Intents.init();
        try (ActivityScenario<MenuActivity> scenario = launch(intent)) {
            onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
            onView(withId(R.id.nav_host_fragment_activity_menu1)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_search)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_search)).perform(click());
        }
        Intents.release();
    }

    @Test
    public void AddFragOpen_whenIconClicked() {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        Intents.init();
        try (ActivityScenario<MenuActivity> scenario = launch(intent)) {
            onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
            onView(withId(R.id.nav_host_fragment_activity_menu1)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_add)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_add)).perform(click());
            onView(withId(R.id.text_add)).check(matches(isDisplayed()));
        }
        Intents.release();
    }

    @Test
    public void FavoritesFragOpen_whenIconClicked() {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        Intents.init();
        try (ActivityScenario<MenuActivity> scenario = launch(intent)) {
            onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
            onView(withId(R.id.nav_host_fragment_activity_menu1)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_favorites)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_favorites)).perform(click());
        }
        Intents.release();
    }

    @Test
    public void MessagesFragOpen_whenIconClicked() {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        Intents.init();
        try (ActivityScenario<MenuActivity> scenario = launch(intent)) {
            onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
            onView(withId(R.id.nav_host_fragment_activity_menu1)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_messages)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_messages)).perform(click());
        }
        Intents.release();
    }
}
