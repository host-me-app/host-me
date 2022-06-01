package ch.epfl.sweng.hostme.utils;

import static android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;
import static ch.epfl.sweng.hostme.utils.Constants.REQ_IMAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

public class ListImage {
    private final static String COMPLETE = "File upload complete";
    private final static String PREVIEW = "preview";
    private static final String ADDED = "Listing created !";
    private static final String DOC_ID = "docId";
    private final static CollectionReference DB = Database.getCollection(APARTMENTS);

    @SuppressLint("StaticFieldLeak")
    private static Fragment fragment;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static int ext;
    private static ArrayList<Uri> imagesUri;

    public static void init(Fragment f, Context c) {
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

    @SuppressLint("NotifyDataSetChanged")
    public static void pushImages(String path, Apartment apartment, List<Apartment> myListings, RecyclerView.Adapter adapter) {
        for (Uri uri : imagesUri) {
            int i = ext++;
            @SuppressLint("DefaultLocale") String ref = String.format("%s/%s%d.jpg", path, PREVIEW, ext);
            StorageReference target = Storage.getStorageReferenceByChild(ref);
            target.putFile(uri).addOnSuccessListener(done -> {
                System.out.println(i);
                Toast.makeText(context, COMPLETE, Toast.LENGTH_SHORT).show();
                if (i == imagesUri.size()) {
                    DB.add(apartment).addOnSuccessListener(doc -> {
                        Map<String, Object> addition = new HashMap<>();
                        addition.put(DOC_ID, doc.getId());
                        doc.update(addition);
                        apartment.setDocId(doc.getId());
                        myListings.add(apartment);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(context, ADDED, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }
}
