package ch.epfl.sweng.hostme;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class ChangePwdTest {


    @Test
    public void enterWrongMail() {
        Intent intent = new Intent(getApplicationContext(), EnterMailChangePwd.class);
        try (ActivityScenario<MainActivity> scenario = launch(intent)) {
            String wrongMail = "jules.magggg@orange.fr";
            onView(withId(R.id.mailForgotPwd)).perform(typeText(wrongMail), closeSoftKeyboard());
            onView(withId(R.id.nextButtonMail2)).perform(click());
        }
    }

    @Test
    public void enterValidMail() {
        Intent intent = new Intent(getApplicationContext(), EnterMailChangePwd.class);
        try (ActivityScenario<MainActivity> scenario = launch(intent)) {
            String validMail = "jules.maglione20@gmail.com";
            onView(withId(R.id.mailForgotPwd)).perform(typeText(validMail), closeSoftKeyboard());
            onView(withId(R.id.nextButtonMail2)).perform(click());
        }
    }
}