package ch.epfl.sweng.hostme.ui.account;

import static android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

import static ch.epfl.sweng.hostme.utils.Constants.CAMERA_PERM_CODE;
import static ch.epfl.sweng.hostme.utils.Constants.DELETE;
import static ch.epfl.sweng.hostme.utils.Constants.PICK_CAM;
import static ch.epfl.sweng.hostme.utils.Constants.PICK_GALLERY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import ch.epfl.sweng.hostme.R;


public class AccountUtils {
    private final Fragment fragment;
    private final ActivityResultLauncher<Intent> activityResultLauncherGallery;
    private final ActivityResultLauncher<Intent> activityResultLauncherCamera;
    private final View view;

    public AccountUtils(Fragment frag, ActivityResultLauncher<Intent> resultLauncherGallery, ActivityResultLauncher<Intent> resultLauncherCamera, View view) {
        this.activityResultLauncherGallery = resultLauncherGallery;
        this.activityResultLauncherCamera = resultLauncherCamera;
        this.fragment = frag;
        this.view = view;
    }

    /**
     * Showing dialog where you can take select to take image from either
     * the Gallery or the Camera
     */
    public void showImagePickDialog() {
        String[] options = {PICK_CAM, PICK_GALLERY, DELETE};
        AlertDialog.Builder builder = new AlertDialog.Builder(this.fragment.requireContext());
        builder.setTitle("Profile Picture");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                this.pickFromCamera();
            } else if (which == 1) {
                this.pickFromGallery();
            } else if (which == 2) {
                this.deleteProfilePicture();
            }
        });
        builder.create().show();
    }

    /**
     * Pick user Profile image from Camera
     */
    private void pickFromCamera() {
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.fragment.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            this.openCamera();
        }
    }

    /**
     * Pick user Profile image from Gallery
     */
    @SuppressLint("IntentReset")
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        activityResultLauncherGallery.launch(galleryIntent);
    }

    /**
     * Delete Profile Picture
     */
    private void deleteProfilePicture() {
        AccountFragment.uri_to_save = null;
        ImageView editProfilePicture = view.findViewById(R.id.user_profile_image);
        editProfilePicture.setImageResource(R.drawable.ic_baseline_account_circle_24);
        if (AccountFragment.profilePicInDB) {
            AccountFragment.deletePic = true;
            Button saveButton = view.findViewById(R.id.user_profile_save_button);
            saveButton.setEnabled(true);
        }
    }

    /**
     * Open Camera
     */
    public void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncherCamera.launch(cameraIntent);
    }


}
