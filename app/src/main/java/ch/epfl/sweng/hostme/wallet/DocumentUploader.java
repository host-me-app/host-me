package ch.epfl.sweng.hostme.wallet;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.firebase.storage.StorageReference;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Storage;

public class DocumentUploader {

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILE_CHOOSER = 2000;
    private static final String UPLOAD_SUCCEED_MESSAGE = "Upload succeed!";
    private static final String UPLOAD_FAILED_MESSAGE = "Upload failed!";
    private static final String PERMISSION_DENIED_MESSAGE = "Permission denied!";
    private static final String CHECK_FAILED_MESSAGE = "Failed to check documents!";
    private final Document document;
    private final String uid;
    private final Activity activity;
    private final Context context;
    private final String titleChooser;
    private final Button buttonBrowse;
    private final Button buttonDownload;
    private final ImageView checkImage;

    public DocumentUploader(Document document, String uid, Activity activity, Context context) {
        this.document = document;
        this.uid = uid + "/";
        this.activity = activity;
        this.context = context;
        this.titleChooser = "Choose" + document.getDocumentName();
        this.buttonBrowse = activity.findViewById(document.getButtonBrowseId());
        this.buttonDownload = activity.findViewById(document.getButtonDownloadId());
        this.checkImage = activity.findViewById(document.getCheckImageId());
        buttonBrowse.setOnClickListener(view -> askPermissionAndBrowseFile());
        this.checkFileUploaded();
    }

    private void askPermissionAndBrowseFile() {
        int permission = ActivityCompat.checkSelfPermission(this.activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            this.activity.requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_REQUEST_CODE_PERMISSION
            );
        }
        this.doBrowseFile();
    }

    private void doBrowseFile() {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType(this.document.getType());
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

        chooseFileIntent = Intent.createChooser(chooseFileIntent, this.titleChooser);
        this.activity.startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILE_CHOOSER);
    }

    public void onPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == MY_REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.doBrowseFile();
            } else {
                Toast.makeText(this.context, PERMISSION_DENIED_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBrowseFileResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_RESULT_CODE_FILE_CHOOSER && resultCode == Activity.RESULT_OK && data != null) {
            this.uploadFile(data.getData());
        }
    }

    private void uploadFile(Uri file) {
        String pathString = this.document.getPath() + this.uid + this.document.getFileName() + this.document.getFileExtension();
        StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);
        fileRef.putFile(file)
                .addOnSuccessListener(taskSnapshot -> {
                    this.checkFileUploaded();
                    Toast.makeText(this.context, UPLOAD_SUCCEED_MESSAGE, Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(exception -> Toast.makeText(this.context, UPLOAD_FAILED_MESSAGE, Toast.LENGTH_SHORT).show());
    }

    private void checkFileUploaded() {
        String pathString = this.document.getPath() + this.uid;
        StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);
        fileRef.listAll()
                .addOnSuccessListener(listResult -> {
                    if (listResult.getItems().size() == 1) {
                        changeButton();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this.context, CHECK_FAILED_MESSAGE, Toast.LENGTH_SHORT).show());
    }

    private void changeButton() {
        this.buttonBrowse.setText(R.string.change_file);
        this.buttonDownload.setVisibility(View.VISIBLE);
        this.checkImage.setVisibility(View.VISIBLE);
    }
}
