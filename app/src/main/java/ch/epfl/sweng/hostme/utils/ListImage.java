package ch.epfl.sweng.hostme.utils;

import static ch.epfl.sweng.hostme.utils.Constants.REQ_IMAGE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;

import ch.epfl.sweng.hostme.database.Storage;

public class ListImage {
    private final static String SELECTION = "Select a display image";
    private final static String COMPLETE = "File upload complete";
    private final static String PREVIEW = "preview";

    private static String path;
    private static Activity activity;
    private static Context context;
    private static int ext;

    public static void init(String p, Activity a, Context c) {
        path = p;
        activity = a;
        context = c;
        ext = 0;
    }

    public static void acceptImage() {
        Intent selectImage = new Intent(Intent.ACTION_GET_CONTENT);
        selectImage.setType("image/jpeg");
        selectImage = selectImage.createChooser(selectImage, SELECTION);
        activity.startActivityForResult(selectImage, REQ_IMAGE);
    }

    public static void onAcceptImage(int res, Uri image) {
        if (res == Activity.RESULT_OK && image != null) {
            pushImage(image);
        }
    }

    private static void pushImage(Uri image) {
        ext++;
        String ref = String.format("%s/%s%d.jpg", path, PREVIEW, ext);
        StorageReference target = Storage.getStorageReferenceByChild(ref);
        target.putFile(image).addOnSuccessListener(done -> {
            Toast.makeText(context, COMPLETE, Toast.LENGTH_SHORT).show();
        });
    }

    public static String getPath() {
        return path;
    }
}
