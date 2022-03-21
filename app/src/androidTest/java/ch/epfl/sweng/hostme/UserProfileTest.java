package ch.epfl.sweng.hostme;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserProfileTest {



    @Test
    public void ProfileInfoIsDisplayedTest() throws InterruptedException {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "profiletest@gmail.com";
            String password = "Test111!";

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            onView(withId(R.id.userName)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));
            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
            onView(withId(R.id.logInButton)).perform(click());

            Thread.sleep(2000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());

            onView(withId(R.id.userProfileLastName)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileEmail)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfilePhone)).check(matches(isDisplayed()));

            onView(withId(R.id.userProfileLastName)).check(matches(withText(mAuth.getCurrentUser().getDisplayName())));
            onView(withId(R.id.userProfileEmail)).check(matches(withText(mAuth.getCurrentUser().getEmail())));

            Thread.sleep(5000);
        }
        Intents.release();
    }

    @Test
    public void logOut() throws InterruptedException {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "profileTest@gmail.com";
            String password = "Test111!";

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            onView(withId(R.id.userName)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));
            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
            onView(withId(R.id.logInButton)).perform(click());

            Thread.sleep(2000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());

            onView(withId(R.id.userProfileLastName)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileEmail)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfilePhone)).check(matches(isDisplayed()));

            onView(withId(R.id.userProfileLastName)).check(matches(withText(mAuth.getCurrentUser().getDisplayName())));
            onView(withId(R.id.userProfileEmail)).check(matches(withText(mAuth.getCurrentUser().getEmail())));

            onView(withId(R.id.userProfilelogOutButton)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfilelogOutButton)).perform(click());

            Thread.sleep(5000);
        }
        Intents.release();
    }


    @Test
    public void saveProfileTest() throws InterruptedException {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "profileTest@gmail.com";
            String password = "Test111!";

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            onView(withId(R.id.userName)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));
            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
            onView(withId(R.id.logInButton)).perform(click());

            Thread.sleep(2000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());

            onView(withId(R.id.userProfileLastName)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileEmail)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfilePhone)).check(matches(isDisplayed()));

            onView(withId(R.id.userProfileLastName)).check(matches(withText(mAuth.getCurrentUser().getDisplayName())));
            onView(withId(R.id.userProfileEmail)).check(matches(withText(mAuth.getCurrentUser().getEmail())));

            //Change Name
            String name = "Nat modified";
            onView(withId(R.id.userProfileLastName)).perform(clearText())
                    .perform(typeText(name), closeSoftKeyboard());

            onView(withId(R.id.userProfileSaveButton)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileSaveButton)).perform(click());

            onView(withId(R.id.userProfilelogOutButton)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfilelogOutButton)).perform(click());

            //Re-Log in

            Thread.sleep(2000);

            onView(withId(R.id.userName)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));
            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
            onView(withId(R.id.logInButton)).perform(click());

            Thread.sleep(2000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());

            onView(withId(R.id.userProfileLastName)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileEmail)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfilePhone)).check(matches(isDisplayed()));

            onView(withId(R.id.userProfileLastName)).check(matches(withText(name)));
            onView(withId(R.id.userProfileEmail)).check(matches(withText(mAuth.getCurrentUser().getEmail())));


            Thread.sleep(5000);
        }
        Intents.release();
    }


    @Test
    public void saveButtonTest() throws InterruptedException {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "profileTest@gmail.com";
            String password = "Test111!";

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            onView(withId(R.id.userName)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));
            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
            onView(withId(R.id.logInButton)).perform(click());

            Thread.sleep(2000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());

            onView(withId(R.id.userProfileLastName)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileEmail)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfilePhone)).check(matches(isDisplayed()));

            onView(withId(R.id.userProfileLastName)).check(matches(withText(mAuth.getCurrentUser().getDisplayName())));
            onView(withId(R.id.userProfileEmail)).check(matches(withText(mAuth.getCurrentUser().getEmail())));

            //Change Name
            String name = mAuth.getCurrentUser().getDisplayName() + "add";

            onView(withId(R.id.userProfileLastName)).perform(clearText())
                    .perform(typeText(name), closeSoftKeyboard());

            onView(withId(R.id.userProfileSaveButton)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileSaveButton)).check(matches(isEnabled()));

            String name2 = mAuth.getCurrentUser().getDisplayName();

            onView(withId(R.id.userProfileLastName)).perform(clearText())
                    .perform(typeText(name2), closeSoftKeyboard());

            onView(withId(R.id.userProfileSaveButton)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileSaveButton)).check(matches(isNotEnabled()));


            onView(withId(R.id.userProfileSaveButton)).perform(click());

            onView(withId(R.id.userProfilelogOutButton)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfilelogOutButton)).perform(click());


            Thread.sleep(5000);
        }
        Intents.release();
    }

}