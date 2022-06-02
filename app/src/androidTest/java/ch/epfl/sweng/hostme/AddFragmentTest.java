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

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
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

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Test
    public void generateApartmentTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            savePickedImage();
            intending(hasAction(Intent.ACTION_PICK)).respondWith(getImageUriResult());

            String usr = "testlogin@gmail.com";
            String pwd = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(usr), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());

            onView(withId(R.id.navigation_add)).perform(click());

            onView(withId(R.id.add_new)).perform((click()));

            onView(withId(R.id.enter_proprietor)).perform(click());
            onView(withId(R.id.enter_proprietor)).perform(typeText("a"), closeSoftKeyboard());
            onView(withId(R.id.enter_name)).perform(click());
            onView(withId(R.id.enter_name)).perform(typeText("b"), closeSoftKeyboard());
            onView(withId(R.id.enter_room)).perform(click());
            onView(withId(R.id.enter_room)).perform(typeText("c"), closeSoftKeyboard());
            onView(withId(R.id.enter_address)).perform(click());
            onView(withId(R.id.enter_address)).perform(typeText("d"), closeSoftKeyboard());
            onView(withId(R.id.enter_npa)).perform(click());
            onView(withId(R.id.enter_npa)).perform(typeText("1"), closeSoftKeyboard());
            onView(withId(R.id.enter_city)).perform(click());
            onView(withId(R.id.enter_city)).perform(typeText("e"), closeSoftKeyboard());
            onView(withId(R.id.enter_rent)).perform(click());
            onView(withId(R.id.enter_rent)).perform(typeText("2"), closeSoftKeyboard());
            onView(withId(R.id.enter_utilities)).perform(click());
            onView(withId(R.id.enter_utilities)).perform(typeText("3"), closeSoftKeyboard());
            onView(withId(R.id.enter_deposit)).perform(click());
            onView(withId(R.id.enter_deposit)).perform(typeText("4"), closeSoftKeyboard());
            onView(withId(R.id.enter_beds)).perform(click());
            onView(withId(R.id.enter_beds)).perform(typeText("5"), closeSoftKeyboard());
            onView(withId(R.id.enter_area)).perform(click());
            onView(withId(R.id.enter_area)).perform(typeText("6"), closeSoftKeyboard());
            onView(withId(R.id.enter_duration)).perform(click());
            onView(withId(R.id.enter_duration)).perform(typeText("7"), closeSoftKeyboard());
            Thread.sleep(1000);
            onView(withId(R.id.enter_images)).check(matches(isEnabled()));
            onView(withId(R.id.enter_images)).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.add_submit)).perform(click());
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    private Instrumentation.ActivityResult getImageUriResult() {
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
        FileOutputStream outStream = null;
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
