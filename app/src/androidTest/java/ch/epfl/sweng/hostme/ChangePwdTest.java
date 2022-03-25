package ch.epfl.sweng.hostme;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class ChangePwdTest {


    @Test
    public void enterWrongMail() {
        Intent intent = new Intent(getApplicationContext(), EnterMailChangePwd.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = launch(intent)) {
            String wrongMail = "host.me@gmail.com";
            onView(withId(R.id.mailForgotPwd)).perform(typeText(wrongMail), closeSoftKeyboard());
        }
        Intents.release();
    }

    @Test
    public void enterValidMail() {
        Intent intent = new Intent(getApplicationContext(), EnterMailChangePwd.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = launch(intent)) {
            String validMail = "host.me.app2022@gmail.com";
            onView(withId(R.id.mailForgotPwd)).perform(typeText(validMail), closeSoftKeyboard());
        }
        Intents.release();
    }
}