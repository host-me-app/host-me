package ch.epfl.sweng.hostme;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserProfileActivityTest {


    @Test
    public void ProfileInfoIsDisplayedTest(){

        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context,UserProfileActivity.class);

        try (ActivityScenario<UserProfileActivity> scenario = ActivityScenario.launch(intent)) {

            onView(withId(R.id.userProfileName)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileName)).check(matches(withText("Steve Gomez")));

            onView(withId(R.id.userProfileEmail)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfileEmail)).check(matches(withText("stv@gmail.com")));

            onView(withId(R.id.userProfilePhone)).check(matches(isDisplayed()));
            onView(withId(R.id.userProfilePhone)).check(matches(withText("+41 78 332 332")));



        }
    }

}