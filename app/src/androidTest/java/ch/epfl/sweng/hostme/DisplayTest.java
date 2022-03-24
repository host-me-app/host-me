package ch.epfl.sweng.hostme;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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
public class DisplayTest {


    @Test
    public void showSearchFragment() throws InterruptedException {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Intents.init();
        try (ActivityScenario<MainActivity> scenario = launch(intent)) {
            String rightMail = "jules.maglione@epfl.ch";
            String pwd = "JUtheo2020!";
            onView(withId(R.id.userName)).perform(typeText(rightMail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());
            Thread.sleep(10000);
        }
        Intents.release();
    }

}