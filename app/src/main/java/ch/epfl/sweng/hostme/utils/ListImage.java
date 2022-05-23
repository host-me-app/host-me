package ch.epfl.sweng.hostme.utils;

import static android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static ch.epfl.sweng.hostme.utils.Constants.REQ_IMAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.storage.StorageReference;

import ch.epfl.sweng.hostme.database.Storage;

public class ListImage {
    private final static String SELECTION = "Select a display image";
    private final static String COMPLETE = "File upload complete";
    private final static String PREVIEW = "preview";

    private static String path;
    @SuppressLint("StaticFieldLeak")
    private static Fragment fragment;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static int ext;

    public static void init(String p, Fragment f, Context c) {
        path = p;
        fragment = f;
        context = c;
        ext = 0;
    }

    @SuppressLint("IntentReset")
    public static void acceptImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/jpeg");
        galleryIntent = Intent.createChooser(galleryIntent, SELECTION);
        fragment.startActivityForResult(galleryIntent, REQ_IMAGE);
    }

    public static void onAcceptImage(int res, Uri image) {
        if (res == Activity.RESULT_OK && image != null) {
            pushImage(image);
        }
    }

    private static void pushImage(Uri image) {
        ext++;
        @SuppressLint("DefaultLocale") String ref = String.format("%s/%s%d.jpg", path, PREVIEW, ext);
        StorageReference target = Storage.getStorageReferenceByChild(ref);
        target.putFile(image).addOnSuccessListener(done -> Toast.makeText(context, COMPLETE, Toast.LENGTH_SHORT).show());
    }

    public static String getPath() {
        return path;
    }
}
