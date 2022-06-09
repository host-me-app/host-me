package ch.epfl.sweng.hostme.chat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.activities.LogInActivity;
import ch.epfl.sweng.hostme.activities.UsersActivity;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

@RunWith(AndroidJUnit4.class)
public class SharedDocumentsTest {

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
    public void ShareDocumentsCancel() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<UsersActivity> scenario = ActivityScenario.launch(intent)) {
            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());

            onView(withId(R.id.navigation_messages)).perform(click());
            onView(withId(R.id.contact_button)).perform(click());
            onView(withId(R.id.users_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            onView(withId(R.id.share_button)).check(matches(isDisplayed()));
            onView(withId(R.id.share_button)).check(matches(isEnabled()));
            onView(withId(R.id.share_button)).perform(click());
            onView(withText("CANCEL")).check(matches(isDisplayed()));
            onView(withText("CANCEL")).perform(click());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void uploadSalarySlipsTestAndShare() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            savePickedPDF();
            intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(getPDFResult());

            String mail = "testlogin@gmail.com";
            String password = "fakePassword1!";

            onView(withId(R.id.user_name)).perform(typeText(mail), closeSoftKeyboard());
            onView(withId(R.id.pwd)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_in_button)).perform(click());

            onView(withId(R.id.navigation_account)).perform(click());

            onView(withId(R.id.wallet_button)).perform(click());
            onView(withId(R.id.sp_import_button)).perform(click());
            Thread.sleep(1000);
            onView(isRoot()).perform(ViewActions.pressBack());

            onView(withId(R.id.navigation_messages)).perform(click());
            onView(withId(R.id.contact_button)).perform(click());
            onView(withId(R.id.users_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            onView(withId(R.id.share_button)).perform(click());
            onView(withText("Salary Slips")).check(matches(isDisplayed()));
            onView(withText("Salary Slips")).perform(click());
            onView(withText("Extract from the Execution Office")).check(matches(isDisplayed()));
            onView(withText("Extract from the Execution Office")).perform(click());
            onView(withText("SHARE")).check(matches(isDisplayed()));
            onView(withText("SHARE")).perform(click());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    private Instrumentation.ActivityResult getPDFResult() {
        Intent resultData = new Intent();
        File dir = ApplicationProvider.getApplicationContext().getExternalCacheDir();
        File file = new File(dir.getPath(), "file.pdf");
        Uri uri = Uri.fromFile(file);
        resultData.setData(uri);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    private void savePickedPDF() {
        Drawable d = ApplicationProvider.getApplicationContext().getResources().getDrawable(R.drawable.add_icon);
        Bitmap bm = drawableToBitmap(d);
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(960, 1280, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(myPageInfo);
        page.getCanvas().drawBitmap(bm, 0, 0, null);
        pdfDocument.finishPage(page);
        File dir = ApplicationProvider.getApplicationContext().getExternalCacheDir();
        File file = new File(dir.getPath(), "file.pdf");
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(file);
            pdfDocument.writeTo(outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
