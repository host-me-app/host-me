package ch.epfl.sweng.hostme.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.GrantPermissionRule;

import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.activities.LogInActivity;
import ch.epfl.sweng.hostme.apartment.Apartment;
import ch.epfl.sweng.hostme.apartment.ListImage;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.fragments.AddFragment;

public class ListImageTest {
    @Rule
    public GrantPermissionRule internetAccess = GrantPermissionRule.grant(android.Manifest.permission.INTERNET, android.Manifest.permission.READ_EXTERNAL_STORAGE);

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void initializeTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> ListImage.init(new AddFragment()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    @Test
    public void acceptImageTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogInActivity.class);
        Intents.init();
        try (ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(intent)) {
            savePickedImage();
            scenario.onActivity(activity -> {
                ListImage.init(new AddFragment());
                ListImage.acceptImage();
                ListImage.onAcceptImage(Activity.RESULT_OK, getClipData());
                assertTrue(ListImage.areImagesSelected());
                try {
                    ListImage.pushImages("a_a_a", createApartment(), new ArrayList<>(), new RecyclerView.Adapter() {
                        @NonNull
                        @Override
                        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            return null;
                        }

                        @Override
                        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

                        }

                        @Override
                        public int getItemCount() {
                            return 0;
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intents.release();
    }

    private Apartment createApartment() throws JSONException {
        JSONObject fields = new JSONObject();
        fields.put(Constants.NAME, "");
        fields.put(Constants.ROOM, "");
        fields.put(Constants.ADDRESS, "");
        fields.put(Constants.NPA, 0);
        fields.put(Constants.CITY, "");
        fields.put(Constants.RENT, 0);
        fields.put(Constants.BEDS, 0);
        fields.put(Constants.AREA, 0);
        fields.put(Constants.FURNISHED, false);
        fields.put(Constants.BATH, "");
        fields.put(Constants.KITCHEN, "");
        fields.put(Constants.LAUNDRY, "");
        fields.put(Constants.PETS, false);
        fields.put(Constants.IMAGE_PATH, "imagePath");
        fields.put(Constants.PROPRIETOR, "");
        fields.put(Constants.UID, "uid");
        fields.put(Constants.UTILITIES, 0);
        fields.put(Constants.DEPOSIT, 0);
        fields.put(Constants.DURATION, 0);

        return new Apartment(fields);
    }

    private ClipData getClipData() {
        File dir = ApplicationProvider.getApplicationContext().getExternalCacheDir();
        File file = new File(dir.getPath(), "pickImageResult2.jpeg");
        Uri uri = Uri.fromFile(file);
        return new ClipData(new ClipDescription(ClipDescription.MIMETYPE_TEXT_URILIST, new String[]{"uri"}), new ClipData.Item(uri));
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
