package ch.epfl.sweng.hostme.ui.account;

import static android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Storage;


public class AccountUtils {


    public static final int CAMERA_PERM_CODE = 101;
    private Activity activity;
    private ActivityResultLauncher<Intent> activityResultLauncherGallery;
    private ActivityResultLauncher<Intent> activityResultLauncherCamera;
    private View view;


    public AccountUtils(Activity act,ActivityResultLauncher<Intent> rl1, ActivityResultLauncher<Intent> rl2, View vw){
        activityResultLauncherGallery = rl1;
        activityResultLauncherCamera = rl2;
        activity = act;
        view =vw;
    }


    /**
     * Showing dialog where you can take select to take image from either
     * the Gallery or the Camera
     */
    public void showImagePickDialog() {
        String options[] = {"Pick from Camera", "Pick from Gallery","Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Profile Picture");
        builder.setItems(options, (dialog, which) -> {

                    if (which == 0) {
                        pickFromCamera();
                    } else if (which == 1) {
                        pickFromGallery();
                    }else if(which==2){
                        deleteProfilePicture();
                    }
                }
        );
        builder.create().show();
    }


    /**
     * Pick user Profile image from Camera
     */
    private void pickFromCamera() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            openCamera();
        }
    }

    /**
     * Pick user Profile image from Gallery
     */
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        activityResultLauncherGallery.launch(galleryIntent);
    }

    /**
     * Delete Profile Picture
     */
    private void deleteProfilePicture(){

        String pathString = "profilePicture/"+ Auth.getUid() +"/profile.jpg";
        StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);
        ImageView editProfilePicture = view.findViewById(R.id.userProfileImage);
        editProfilePicture.setImageResource(R.drawable.ic_baseline_account_circle_24);
        AccountFragment.uri_to_save = null;
        if(AccountFragment.profilePicinDb) {
            AccountFragment.deletePic = true;
            Button saveButton = view.findViewById(R.id.userProfileSaveButton);
            saveButton.setEnabled(true);
        }
    }

    /**
     * Open Camera
     */
    public void openCamera() {
        Toast.makeText(activity, "Camera Open Request", Toast.LENGTH_SHORT).show();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncherCamera.launch(cameraIntent);
    }



}
