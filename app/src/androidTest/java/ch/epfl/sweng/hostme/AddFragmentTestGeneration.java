package ch.epfl.sweng.hostme;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

@RunWith(AndroidJUnit4.class)
public class AddFragmentTestGeneration {

    @Rule
    public GrantPermissionRule accessRule = GrantPermissionRule.grant(android.Manifest.permission.INTERNET,
            android.Manifest.permission.READ_EXTERNAL_STORAGE);

    @Rule
    public IntentsTestRule<LogInActivity> activityRule = new IntentsTestRule<>(LogInActivity.class);

    @Before
    public void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
        savePickedImage();
        intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(getImageResult());
    }

    @Test
    public void generateApartmentTest() {
        String usr = "testlogin@gmail.com";
        String pwd = "fakePassword1!";

        onView(withId(R.id.userName)).perform(typeText(usr), closeSoftKeyboard());
        onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
        onView(withId(R.id.logInButton)).perform(click());

        onView(withId(R.id.navigation_add)).perform(click());

        onView(withId(R.id.add_new)).perform((click()));

        onView(withId(R.id.enter_proprietor)).perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.enter_name)).perform(typeText("b"), closeSoftKeyboard());
        onView(withId(R.id.enter_room)).perform(typeText("c"), closeSoftKeyboard());
        onView(withId(R.id.enter_address)).perform(typeText("d"), closeSoftKeyboard());
        onView(withId(R.id.enter_npa)).perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.enter_city)).perform(typeText("e"), closeSoftKeyboard());
        onView(withId(R.id.enter_rent)).perform(typeText("2"), closeSoftKeyboard());
        onView(withId(R.id.enter_utilities)).perform(typeText("3"), closeSoftKeyboard());
        onView(withId(R.id.enter_deposit)).perform(typeText("4"), closeSoftKeyboard());
        onView(withId(R.id.enter_beds)).perform(typeText("5"), closeSoftKeyboard());
        onView(withId(R.id.enter_area)).perform(typeText("6"), closeSoftKeyboard());
        onView(withId(R.id.enter_duration)).perform(typeText("7"), closeSoftKeyboard());
        onView(withId(R.id.enter_area)).perform(typeText("6"), closeSoftKeyboard());

        onView(withId(R.id.enter_images)).check(matches(isEnabled()));
        onView(withId(R.id.enter_images)).perform(click());
        onView(withId(R.id.add_submit)).perform(click());
    }

    private Instrumentation.ActivityResult getImageResult() {
        Intent resultData = new Intent();
        File dir = activityRule.getActivity().getExternalCacheDir();
        File file = new File(dir.getPath(), "pickImageResult.jpeg");
        Uri uri = Uri.fromFile(file);
        resultData.setData(uri);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void savePickedImage() {
        Drawable d = activityRule.getActivity().getResources().getDrawable(R.drawable.add_icon);
        Bitmap bm = drawableToBitmap(d);
        assertNotNull(bm);
        File dir = activityRule.getActivity().getExternalCacheDir();
        File file = new File(dir.getPath(), "pickImageResult.jpeg");
        FileOutputStream outStream = null;
        try{
            outStream  = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
