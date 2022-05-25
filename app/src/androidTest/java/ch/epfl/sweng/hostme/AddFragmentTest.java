package ch.epfl.sweng.hostme;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.app.Activity;
import android.app.Instrumentation;
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

import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

@RunWith(AndroidJUnit4.class)
public class AddFragmentTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void initialStateTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String usr = "testlogin@gmail.com";
            String pwd = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(usr), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());

            onView(withId(R.id.navigation_add)).perform(click());

            onView(withId(R.id.add_new)).check(matches(isDisplayed()));
            onView(withId(R.id.add_new)).check(matches(isClickable()));
            onView(withId(R.id.add_form)).check(matches(withEffectiveVisibility(Visibility.GONE)));
            onView(withId(R.id.add_buttons)).check(matches(withEffectiveVisibility(Visibility.GONE)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void openFormTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String usr = "testlogin@gmail.com";
            String pwd = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(usr), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());

            onView(withId(R.id.navigation_add)).perform(click());

            onView(withId(R.id.add_new)).perform((click()));

            onView(withId(R.id.add_form)).check(matches(isDisplayed()));
            onView(withId(R.id.add_buttons)).check(matches(isDisplayed()));
            onView(withId(R.id.enter_images)).check(matches(isNotEnabled()));
            onView(withId(R.id.add_submit)).check(matches(isNotEnabled()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void incompleteFormTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
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

            onView(withId(R.id.enter_images)).check(matches(isNotEnabled()));
            onView(withId(R.id.add_submit)).check(matches(isNotEnabled()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void openAndCloseForm() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String usr = "testlogin@gmail.com";
            String pwd = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(usr), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());

            onView(withId(R.id.navigation_add)).perform(click());

            onView(withId(R.id.add_new)).perform((click()));
            onView(withId(R.id.add_new)).perform((click()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void removeFieldApartmentTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {

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
            onView(withId(R.id.enter_area)).perform(clearText());
            onView(withId(R.id.enter_duration)).perform(typeText("7"), closeSoftKeyboard());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
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
            Thread.sleep(1000);
            onView(withId(R.id.enter_images)).check(matches(isEnabled()));
            onView(withId(R.id.enter_images)).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.add_submit)).check(matches(isEnabled()));
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
        Drawable d = ApplicationProvider.getApplicationContext().getResources().getDrawable(R.drawable.add_icon);
        Bitmap bm = drawableToBitmap(d);
        assertNotNull(bm);
        File dir = ApplicationProvider.getApplicationContext().getExternalCacheDir();
        File file = new File(dir.getPath(), "pickImageResult.jpeg");
        FileOutputStream outStream = null;
        try{
            outStream  = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void listingOwnedTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String usr = "testlogin@gmail.com";
            String pwd = "fakePassword1!";

            onView(withId(R.id.userName)).perform(typeText(usr), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());

            onView(withId(R.id.navigation_add)).perform(click());

            onView(withId(R.id.owner_view)).check(matches(isDisplayed()));
            onView(withId(R.id.add_first)).check(matches(withEffectiveVisibility(Visibility.GONE)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void notOwnerTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String usr = "testchat@gmail.com";
            String pwd = "Hostme@2022";

            onView(withId(R.id.userName)).perform(typeText(usr), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(pwd), closeSoftKeyboard());
            onView(withId(R.id.logInButton)).perform(click());

            onView(withId(R.id.navigation_add)).perform(click());

            onView(withId(R.id.add_first)).check(matches(isDisplayed()));
            onView(withId(R.id.owner_view)).check(matches(withEffectiveVisibility(Visibility.GONE)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

}
