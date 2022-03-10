package ch.epfl.sweng.hostme;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class UserCreationPage3Test {

    @Test
    public void checkLastNamePage() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserCreationPage3.class);

        try (ActivityScenario<UserCreationPage3> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.nextButtonLastName)).perform(click());
            onView(withId(R.id.nextButtonMail)).check(matches(isDisplayed()));
        }
    }

}


