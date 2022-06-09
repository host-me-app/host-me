package ch.epfl.sweng.hostme.chat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.FirebaseApp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.activities.LogInActivity;
import ch.epfl.sweng.hostme.activities.UsersActivity;
import ch.epfl.sweng.hostme.apartment.DisplayApartmentTest;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

@RunWith(AndroidJUnit4.class)
public class ChatActivityTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @AfterClass
    public static void after_class() {
        Intents.release();
    }


    @Test
    public void nameChatDisplayed() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<UsersActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_messages)).perform(click());
            onView(withId(R.id.contact_button)).check(matches(isDisplayed()));
            onView(withId(R.id.contact_button)).check(matches(isEnabled()));
            onView(withId(R.id.contact_button)).perform(click());
            onView(withId(R.id.users_recycler_view)).check(new DisplayApartmentTest.RecyclerViewMinItemCountAssertion(1));
            onView(withId(R.id.users_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            onView(withId(R.id.text_name)).check(matches(isDisplayed()));
            onView(withId(R.id.view_background)).check(matches(isDisplayed()));
            onView(withId(R.id.layout_send)).check(matches(isDisplayed()));
            onView(withId(R.id.send_button)).check(matches(isDisplayed()));
            onView(withId(R.id.send_button)).check(matches(isEnabled()));
            onView(withId(R.id.input_message)).check(matches(isDisplayed()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void MessageIsDisplayedChat() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<UsersActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_messages)).perform(click());
            onView(withId(R.id.contact_button)).perform(click());
            onView(withId(R.id.users_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            String message = "hello";
            onView(withId(R.id.input_message)).perform(typeText(message), closeSoftKeyboard());
            onView(withId(R.id.send_button)).perform(click());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void goBackFromConversation() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<UsersActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_messages)).perform(click());
            onView(withId(R.id.contact_button)).perform(click());
            onView(withId(R.id.users_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
            onView(isRoot()).perform(ViewActions.pressBack());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
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
