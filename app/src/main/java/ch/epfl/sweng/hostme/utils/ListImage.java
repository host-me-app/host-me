package ch.epfl.sweng.hostme.utils;

import static android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
import static ch.epfl.sweng.hostme.utils.Constants.REQ_IMAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import ch.epfl.sweng.hostme.database.Storage;

public class ListImage {
    private final static String COMPLETE = "File upload complete";
    private final static String PREVIEW = "preview";

    @SuppressLint("StaticFieldLeak")
    private static Fragment fragment;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static int ext;
    private static ArrayList<Uri> imagesUri;

    public static void init( Fragment f, Context c) {
        fragment = f;
        context = c;
        ext = 0;
        imagesUri = new ArrayList<>();
    }

    @SuppressLint("IntentReset")
    public static void acceptImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setType("image/jpeg");
        fragment.startActivityForResult(galleryIntent, REQ_IMAGE);
    }

    public static void onAcceptImage(int res, ClipData images) {
        if (res == Activity.RESULT_OK && images != null) {
            int count = images.getItemCount();
            for (int i = 0; i < count; i++) {
                Uri image = images.getItemAt(i).getUri();
                saveImage(image);
            }
        }
    }

    public static boolean areImagesSelected() {
        return !imagesUri.isEmpty();
    }

    public static void clear() {
        ext = 0;
        imagesUri.clear();
    }

    private static void saveImage(Uri image) {
        imagesUri.add(image);
    }

    public static void pushImages(String path) {
        for (Uri uri : imagesUri) {
            ext++;
            @SuppressLint("DefaultLocale") String ref = String.format("%s/%s%d.jpg", path, PREVIEW, ext);
            StorageReference target = Storage.getStorageReferenceByChild(ref);
            target.putFile(uri).addOnSuccessListener(done -> Toast.makeText(context, COMPLETE, Toast.LENGTH_SHORT).show());
        }
    }
}
