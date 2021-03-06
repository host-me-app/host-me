package ch.epfl.sweng.hostme.account;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

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
import ch.epfl.sweng.hostme.activities.MenuActivity;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

@RunWith(AndroidJUnit4.class)
public class UserProfileUITest {

    @Rule
    public GrantPermissionRule accessRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA);

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
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

    @Test
    public void ProfileInfoIsDisplayedTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";
            String firstName = "test";
            String lastName = "account";
            String gender = "Male";

            onView(ViewMatchers.withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());
            onView(withId(R.id.user_profile_first_name)).check(matches(withText(firstName)));
            onView(withId(R.id.user_profile_last_name)).check(matches(withText(lastName)));
            onView(withId(R.id.user_profile_email)).check(matches(withText(Auth.getCurrentUser().getEmail())));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void logOut() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).perform(click());
            onView(withId(R.id.user_profile_log_out_button)).perform(click());
            Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), MenuActivity.class);
            ActivityScenario<MenuActivity> scenario2 = ActivityScenario.launch(intent2);

            onView(withId(R.id.log_in_button)).check(matches(isDisplayed()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void saveProfileButtonEnabledTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.navigation_account)).perform(click());

            String firstname = "test modified";
            onView(withId(R.id.user_profile_first_name)).perform(clearText()).perform(typeText(firstname), closeSoftKeyboard());
            onView(withId(R.id.user_profile_save_button)).check(matches(isEnabled()));
            onView(withId(R.id.user_profile_save_button)).check(matches(isDisplayed()));
            onView(withId(R.id.user_profile_gender_f)).perform(click());
            onView(withId(R.id.user_profile_save_button)).perform(click());

            String firstnameOriginal = "test";
            onView(withId(R.id.user_profile_first_name)).perform(clearText()).perform(typeText(firstnameOriginal), closeSoftKeyboard());
            onView(withId(R.id.user_profile_gender_m)).perform(click());
            onView(withId(R.id.user_profile_save_button)).perform(click());
            onView(withId(R.id.user_profile_save_button)).check(matches(isNotEnabled()));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void saveProfileButtonDisabledTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).perform(click());
            String firstname = "test modified";
            onView(withId(R.id.user_profile_first_name)).perform(clearText()).perform(typeText(firstname), closeSoftKeyboard());
            onView(withId(R.id.user_profile_save_button)).check(matches(isEnabled()));
            onView(withId(R.id.user_profile_save_button)).check(matches(isDisplayed()));
            onView(withId(R.id.user_profile_gender_f)).perform(click());
            onView(withId(R.id.user_profile_gender_m)).perform(click());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void checkButtonWalletWorks() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).perform(click());
            onView(withId(R.id.wallet_button)).check(matches(isDisplayed()));
            onView(withId(R.id.wallet_button)).perform(click());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void changePasswordButtonTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String originalPassword = "fakePassword1!";
            String newPassword = "!Hostme2022";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(originalPassword), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_account)).perform(click());
            onView(withId(R.id.user_profile_change_password_button)).perform(click());
            onView(withId(R.id.user_profile_change_pwd_terminate)).perform(click());
            onView(withId(R.id.user_profile_old_password)).check(matches(isDisplayed()));
            onView(withId(R.id.user_profile_new_password)).check(matches(isDisplayed()));
            onView(withId(R.id.user_profile_confirm_new_password)).check(matches(isDisplayed()));
            onView(withId(R.id.user_profile_old_password)).perform(typeText(originalPassword), closeSoftKeyboard());
            onView(withId(R.id.user_profile_new_password)).perform(typeText(newPassword), closeSoftKeyboard());
            onView(withId(R.id.user_profile_confirm_new_password)).perform(typeText(newPassword), closeSoftKeyboard());
            onView(withId(R.id.user_profile_change_pwd_terminate)).perform(click());

            onView(withId(R.id.user_profile_old_password)).perform(clearText()).perform(typeText(newPassword), closeSoftKeyboard());
            onView(withId(R.id.user_profile_new_password)).perform(clearText()).perform(typeText(originalPassword), closeSoftKeyboard());
            onView(withId(R.id.user_profile_confirm_new_password)).perform(clearText()).perform(typeText(originalPassword), closeSoftKeyboard());
            onView(withId(R.id.user_profile_change_pwd_terminate)).perform(click());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void uploadProfilePictureFromCamera() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            savePickedImage();
            intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(getImageResult());

            String mail = "testlogin@gmail.com";
            String originalPassword = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(originalPassword), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).perform(click());

            onView(withId(R.id.user_profile_change_photo_button)).perform(click());
            onView(withText("Pick from Camera")).perform(click());
            intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void uploadProfilePictureFromGallery() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            savePickedImage();
            intending(hasAction(Intent.ACTION_PICK)).respondWith(getImageUriResult());
            String mail = "testlogin@gmail.com";
            String originalPassword = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(originalPassword), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).perform(click());

            onView(withId(R.id.user_profile_change_photo_button)).perform(click());

            onView(withText("Pick from Gallery")).perform(click());
            intended(hasAction(Intent.ACTION_PICK));
            Thread.sleep(1000);
            onView(withId(R.id.user_profile_save_button)).perform(click());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void updateProfilePictureFromGallery() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            savePickedImage();
            intending(hasAction(Intent.ACTION_PICK)).respondWith(getImageUriResult());
            String mail = "testlogin@gmail.com";
            String originalPassword = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(originalPassword), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).perform(click());

            onView(withId(R.id.user_profile_change_photo_button)).perform(click());


            onView(withText("Pick from Gallery")).perform(click());
            intended(hasAction(Intent.ACTION_PICK));
            Thread.sleep(1000);
            onView(withId(R.id.user_profile_save_button)).perform(click());
            onView(withId(R.id.user_profile_change_photo_button)).perform(click());

            onView(withText("Pick from Gallery")).perform(click());
            onView(withId(R.id.user_profile_save_button)).perform(click());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void uploadProfilePictureAndDelete() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            savePickedImage();
            intending(hasAction(Intent.ACTION_PICK)).respondWith(getImageUriResult());
            String mail = "testlogin@gmail.com";
            String originalPassword = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(originalPassword), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());
            Thread.sleep(1000);

            onView(withId(R.id.navigation_account)).perform(click());

            onView(withId(R.id.user_profile_change_photo_button)).perform(click());

            onView(withText("Pick from Gallery")).perform(click());
            intended(hasAction(Intent.ACTION_PICK));
            Thread.sleep(1000);
            onView(withId(R.id.user_profile_save_button)).perform(click());

            onView(withId(R.id.user_profile_change_photo_button)).perform(click());
            onView(withText("Delete")).perform(click());
            onView(withId(R.id.user_profile_save_button)).perform(click());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    private Instrumentation.ActivityResult getImageResult() {
        Bundle bundle = new Bundle();
        Drawable d = ApplicationProvider.getApplicationContext().getResources().getDrawable(R.drawable.add_icon);
        Bitmap bm = drawableToBitmap(d);
        bundle.putParcelable("data", bm);
        Intent resultData = new Intent();
        resultData.putExtras(bundle);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    private Instrumentation.ActivityResult getImageUriResult() {
        Intent resultData = new Intent();
        File dir = ApplicationProvider.getApplicationContext().getExternalCacheDir();
        File file = new File(dir.getPath(), "pickImageResult.jpeg");
        Uri uri = Uri.fromFile(file);
        resultData.setData(uri);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    private void savePickedImage() {
        Drawable d = ApplicationProvider.getApplicationContext().getResources().getDrawable(R.drawable.add_icon);
        Bitmap bm = drawableToBitmap(d);
        assertNotNull(bm);
        File dir = ApplicationProvider.getApplicationContext().getExternalCacheDir();
        File file = new File(dir.getPath(), "pickImageResult.jpeg");
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
