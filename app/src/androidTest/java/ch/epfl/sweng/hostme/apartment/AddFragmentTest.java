package ch.epfl.sweng.hostme.apartment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.KeyEvent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.activities.LogInActivity;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

@RunWith(AndroidJUnit4.class)
public class AddFragmentTest {

    @Rule
    public GrantPermissionRule accessRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE);

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void generateApartmentTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            savePickedImage();
            intending(hasAction(Intent.ACTION_PICK)).respondWith(getClipData());
            String usr = "testlogin@gmail.com";
            String pwd = "fakePassword1!";

            onView(ViewMatchers.withId(R.id.user_name)).perform(typeText(usr), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());

            onView(withId(R.id.navigation_add)).perform(click());

            onView(withId(R.id.add_new)).check(matches(isDisplayed()));
            onView(withId(R.id.add_new)).check(matches(isEnabled()));
            onView(withId(R.id.add_new)).perform((click()));

            onView(withId(R.id.enter_proprietor)).perform(typeText("proprietor"), closeSoftKeyboard());
            onView(withId(R.id.enter_name)).perform(scrollTo(), click());
            onView(withId(R.id.enter_name)).perform(typeText("name"), closeSoftKeyboard());
            onView(withId(R.id.enter_room)).perform(scrollTo(), click());
            onView(withId(R.id.enter_room)).perform(typeText("room"), closeSoftKeyboard());
            onView(withId(R.id.enter_address)).perform(scrollTo(), click());
            onView(withId(R.id.enter_address)).perform(typeText("address"), closeSoftKeyboard());
            onView(withId(R.id.enter_npa)).perform(scrollTo(), click());
            onView(withId(R.id.enter_npa)).perform(pressKey(KeyEvent.KEYCODE_9), closeSoftKeyboard());
            onView(withId(R.id.enter_city)).perform(scrollTo(), click());
            onView(withId(R.id.enter_city)).perform(typeText("city"), closeSoftKeyboard());
            onView(withId(R.id.enter_rent)).perform(scrollTo(), click());
            onView(withId(R.id.enter_rent)).perform(pressKey(KeyEvent.KEYCODE_9), closeSoftKeyboard());
            onView(withId(R.id.enter_utilities)).perform(scrollTo(), click());
            onView(withId(R.id.enter_utilities)).perform(pressKey(KeyEvent.KEYCODE_9), closeSoftKeyboard());
            onView(withId(R.id.enter_deposit)).perform(scrollTo(), click());
            onView(withId(R.id.enter_deposit)).perform(pressKey(KeyEvent.KEYCODE_9), closeSoftKeyboard());
            onView(withId(R.id.enter_beds)).perform(scrollTo(), click());
            onView(withId(R.id.enter_beds)).perform(pressKey(KeyEvent.KEYCODE_9), closeSoftKeyboard());
            onView(withId(R.id.enter_area)).perform(scrollTo(), click());
            onView(withId(R.id.enter_area)).perform(pressKey(KeyEvent.KEYCODE_9), closeSoftKeyboard());
            onView(withId(R.id.enter_duration)).perform(scrollTo(), click());
            onView(withId(R.id.enter_duration)).perform(pressKey(KeyEvent.KEYCODE_9), closeSoftKeyboard());

            onView(withId(R.id.enter_images)).check(matches(isEnabled()));
            onView(withId(R.id.enter_images)).perform(scrollTo(), click());

            intended(hasAction(Intent.ACTION_PICK));

            onView(withId(R.id.add_submit)).check(matches(isEnabled()));
            onView(withId(R.id.add_submit)).perform(scrollTo(), click());
        }
        Intents.release();
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private Instrumentation.ActivityResult getClipData() {
        Intent resultData = new Intent();
        File dir = ApplicationProvider.getApplicationContext().getExternalCacheDir();
        File file = new File(dir.getPath(), "pickImageResult2.jpeg");
        Uri uri = Uri.fromFile(file);
        ClipData clip = new ClipData(new ClipDescription(ClipDescription.MIMETYPE_TEXT_URILIST, new String[]{"uri"}), new ClipData.Item(uri));
        resultData.setClipData(clip);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    private void savePickedImage() {
        Drawable d = ApplicationProvider.getApplicationContext().getResources().getDrawable(R.drawable.add_icon);
        Bitmap bm = drawableToBitmap(d);
        assertNotNull(bm);
        File dir = ApplicationProvider.getApplicationContext().getExternalCacheDir();
        File file = new File(dir.getPath(), "pickImageResult2.jpeg");
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
