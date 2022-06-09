package ch.epfl.sweng.hostme.apartment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.FirebaseApp;

import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.activities.MenuActivity;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

@RunWith(AndroidJUnit4.class)
public class FavoritesTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void removeFavFromRecyclerView() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MenuActivity.class);
        Intents.init();
        try (ActivityScenario<MenuActivity> scenario = ActivityScenario.launch(intent)) {

            onView(withId(R.id.search_recycler_view)).check(new DisplayApartmentTest.RecyclerViewMinItemCountAssertion(1));
            onView(ViewMatchers.withId(R.id.search_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.button_favourite)));
            onView(withId(R.id.navigation_favorites)).perform(click());
            onView(withId(R.id.favorites_recyclerView)).check(new DisplayApartmentTest.RecyclerViewMinItemCountAssertion(1));
            onView(withId(R.id.favorites_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.button_favourite)));

        }
        Intents.release();
    }

    @Test
    public void clickOnFavoriteButtonWithFavoritesDisplayed() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MenuActivity.class);
        Intents.init();
        try (ActivityScenario<MenuActivity> scenario = ActivityScenario.launch(intent)) {

            onView(withId(R.id.search_recycler_view)).check(new DisplayApartmentTest.RecyclerViewMinItemCountAssertion(1));
            onView(withId(R.id.search_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.button_favourite)));
            onView(withId(R.id.navigation_favorites)).perform(click());
            onView(withId(R.id.favorites_recyclerView)).check(new DisplayApartmentTest.RecyclerViewMinItemCountAssertion(1));
            onView(withId(R.id.navigation_account)).perform(click());
            onView(withId(R.id.user_profile_log_out_button)).perform(click());
            Thread.sleep(1000);

            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.navigation_favorites)).perform(click());
            onView(withId(R.id.favorites_recyclerView)).check(new DisplayApartmentTest.RecyclerViewMinItemCountAssertion(1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void deleteApartFromFav() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MenuActivity.class);
        Intents.init();
        try (ActivityScenario<MenuActivity> scenario = ActivityScenario.launch(intent)) {

            onView(withId(R.id.search_recycler_view)).check(new DisplayApartmentTest.RecyclerViewMinItemCountAssertion(1));
            onView(withId(R.id.search_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.button_favourite)));
            onView(withId(R.id.search_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.button_favourite)));
            onView(withId(R.id.navigation_favorites)).perform(click());
            onView(withId(R.id.navigation_account)).perform(click());
            onView(withId(R.id.user_profile_log_out_button)).perform(click());
            Thread.sleep(1000);

            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.navigation_account)).perform(click());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    public static class MyViewAction {

        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }
    }

    public static class RecyclerViewMinItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewMinItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assert adapter != null;
            assertTrue(expectedCount < adapter.getItemCount());
        }
    }
}