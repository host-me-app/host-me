package ch.epfl.sweng.hostme;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
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
public class UserProfileUITest {



    @Test
    public void ProfileInfoIsDisplayedTest() throws InterruptedException {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "acctest@gmail.com";
            String password = "Test111!";
            String firstName = "Joe";
            String lastName = "Test";
            String gender = "Male";


            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            onView(withId(R.id.userName)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));
            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
            onView(withId(R.id.logInButton)).perform(click());

            Thread.sleep(4000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());

            Thread.sleep(5000);
            onView(withId(R.id.userProfileFirstName)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileLastName)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileEmail)).check(matches(isDisplayed()));

            onView(withId(R.id.userProfileFirstName)).check(matches(withText(firstName)));
            onView(withId(R.id.userProfileLastName)).check(matches(withText(lastName)));
            onView(withId(R.id.userProfileEmail)).check(matches(withText(mAuth.getCurrentUser().getEmail())));

            Thread.sleep(1000);
        }
        Intents.release();
    }

    @Test
    public void logOut() throws InterruptedException {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "acctest@gmail.com";
            String password = "Test111!";

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            onView(withId(R.id.userName)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));
            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
            onView(withId(R.id.logInButton)).perform(click());

            Thread.sleep(3000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());

            onView(withId(R.id.userProfilelogOutButton)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfilelogOutButton)).perform(click());

            Thread.sleep(5000);
        }
        Intents.release();
    }


    @Test
    public void saveProfileButtonEnabledTest() throws InterruptedException {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "acctest@gmail.com";
            String password = "Test111!";

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            onView(withId(R.id.userName)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));
            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
            onView(withId(R.id.logInButton)).perform(click());

            Thread.sleep(3000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());

            Thread.sleep(5000);

            //Change FirstName
            String firstname = "Nat modified";
            onView(withId(R.id.userProfileFirstName)).perform(clearText())
                    .perform(typeText(firstname), closeSoftKeyboard());

            //Check button is Enable
            onView(withId(R.id.userProfileSaveButton)).check(matches(isEnabled()));


            //Re put the original FirstName
            String firstnameOriginal = "Joe";
            onView(withId(R.id.userProfileFirstName)).perform(clearText())
                    .perform(typeText(firstnameOriginal), closeSoftKeyboard());


            onView(withId(R.id.userProfilelogOutButton)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfilelogOutButton)).perform(click());


            //Re-Log in

            Thread.sleep(5000);

            onView(withId(R.id.userName)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));
            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
            onView(withId(R.id.logInButton)).perform(click());

            Thread.sleep(3000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());

            Thread.sleep(5000);
            onView(withId(R.id.userProfileFirstName)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileEmail)).check(matches(isDisplayed()));

            onView(withId(R.id.userProfileFirstName)).check(matches(withText(firstnameOriginal)));
            onView(withId(R.id.userProfileEmail)).check(matches(withText(mail)));


            Thread.sleep(5000);
        }
        Intents.release();
    }


    @Test
    public void changePasswordButtonTest() throws InterruptedException {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "acctest@gmail.com";
            String password = "Test111!";

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            onView(withId(R.id.userName)).check(matches(isDisplayed()));
            onView(withId(R.id.pwd)).check(matches(isDisplayed()));
            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
            onView(withId(R.id.logInButton)).perform(click());

            Thread.sleep(3000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());

            Thread.sleep(5000);

            onView(withId(R.id.userProfileEmail)).check(matches(isDisplayed()));

            onView(withId(R.id.userProfileChangePasswordButton)).perform(click());


            onView(withId(R.id.userProfileOldPassword)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileNewPassword)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileConfirmNewPassword)).check(matches(isDisplayed()));

            Thread.sleep(5000);
        }
        Intents.release();
    }

}