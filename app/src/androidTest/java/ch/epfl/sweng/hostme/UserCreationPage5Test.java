package ch.epfl.sweng.hostme;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class UserCreationPage5Test {

    @Test
    public void checkPwdPage() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserCreationPage5.class);

        try (ActivityScenario<UserCreationPage5> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.terminateButton)).perform(click());
        }
    }

}


