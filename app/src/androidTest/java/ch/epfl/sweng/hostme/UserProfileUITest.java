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

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.userCreation.MainActivity;

@RunWith(AndroidJUnit4.class)
public class UserProfileUITest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void ProfileInfoIsDisplayedTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";
            String firstName = "test";
            String lastName = "account";
            String gender = "Male";

            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.userProfileFirstName)).check(matches(withText(firstName)));
            onView(withId(R.id.userProfileLastName)).check(matches(withText(lastName)));
            onView(withId(R.id.userProfileEmail)).check(matches(withText(Auth.getCurrentUser().getEmail())));
        }  catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void logOut() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.userProfilelogOutButton)).perform(click());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void saveProfileButtonEnabledTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).perform(click());
            Thread.sleep(1000);

            String firstname = "test modified";
            onView(withId(R.id.userProfileFirstName)).perform(clearText())
                    .perform(typeText(firstname), closeSoftKeyboard());
            onView(withId(R.id.userProfileSaveButton)).check(matches(isEnabled()));
            onView(withId(R.id.userProfileSaveButton)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileGenderF)).perform(click());
            onView(withId(R.id.userProfileSaveButton)).perform(click());
            Thread.sleep(1000);

            String firstnameOriginal = "test";
            onView(withId(R.id.userProfileFirstName)).perform(clearText())
                    .perform(typeText(firstnameOriginal), closeSoftKeyboard());
            onView(withId(R.id.userProfileGenderM)).perform(click());
            onView(withId(R.id.userProfileSaveButton)).perform(click());
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void checkButtonWalletWorks() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.wallet_button)).check(matches(isDisplayed()));
            onView(withId(R.id.wallet_button)).perform(click());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void changePasswordButtonTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String originalPassword = "fakePassword1!";
            String newPassword = "!Hostme2022";

            onView(withId(R.id.userName)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(originalPassword), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.userProfileChangePasswordButton)).perform(click());
            onView(withId(R.id.userProfileChangePsswdTerminate)).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.userProfileOldPassword)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileNewPassword)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileConfirmNewPassword)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileOldPassword)).perform(typeText(originalPassword), closeSoftKeyboard());
            onView(withId(R.id.userProfileNewPassword)).perform(typeText(newPassword), closeSoftKeyboard());
            onView(withId(R.id.userProfileConfirmNewPassword)).perform(typeText(newPassword), closeSoftKeyboard());
            onView(withId(R.id.userProfileChangePsswdTerminate)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.userProfileOldPassword)).perform(clearText()).perform(typeText(newPassword), closeSoftKeyboard());
            onView(withId(R.id.userProfileNewPassword)).perform(clearText()).perform(typeText(originalPassword), closeSoftKeyboard());
            onView(withId(R.id.userProfileConfirmNewPassword)).perform(clearText()).perform(typeText(originalPassword), closeSoftKeyboard());
            onView(withId(R.id.userProfileChangePsswdTerminate)).perform(click());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

}
