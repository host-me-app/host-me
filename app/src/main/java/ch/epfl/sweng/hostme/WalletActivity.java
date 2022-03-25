package ch.epfl.sweng.hostme;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;


public class WalletActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILE_CHOOSER = 2000;

    private FirebaseStorage storage;
    private String uid;

    private Button buttonBrowse;
    private Button buttonDownload;
    private ImageView check;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        storage = FirebaseStorage.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid() + "/";
        buttonBrowse = findViewById(R.id.button_browse);
        buttonDownload = findViewById(R.id.button_download);
        check = findViewById(R.id.check_residence_permit);

        buttonBrowse.setOnClickListener(view -> askPermissionAndBrowseFile());
        buttonDownload.setOnClickListener(view -> {
            try {
                download();
            } catch (IOException e) {
                Toast.makeText(this, "Download failed 3!", Toast.LENGTH_SHORT).show();
            }
        });
        checkFileUploaded();
    }

    private void askPermissionAndBrowseFile() {
        int permisson = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permisson != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_REQUEST_CODE_PERMISSION
            );
            return;
        }
        this.doBrowseFile();
    }

    private void doBrowseFile() {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("application/pdf");
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILE_CHOOSER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.doBrowseFile();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_RESULT_CODE_FILE_CHOOSER) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri file = data.getData();
                    StorageReference fileRef = storage.getReference().child("documents/residence_permit/" + uid + "residence_permit.pdf");
                    UploadTask uploadTask = fileRef.putFile(file);
                    uploadTask.addOnFailureListener(exception -> {
                        Toast.makeText(this, "Upload failed!", Toast.LENGTH_SHORT).show();
                    }).addOnSuccessListener(taskSnapshot -> {
                        checkFileUploaded();
                        Toast.makeText(this, "Upload successed!", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void download() throws IOException {
        StorageReference strRef = storage.getReference().child("documents/residence_permit/" + uid + "residence_permit.pdf");

        strRef.getDownloadUrl().addOnSuccessListener(uri -> {
            downloadFile(this, "residence_permit", ".pdf", Environment.DIRECTORY_DOWNLOADS, uri.toString());
            Toast.makeText(this, "Download successed!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(exception -> {
            Toast.makeText(this, "Download failed!", Toast.LENGTH_SHORT).show();
        });
    }

    private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        downloadManager.enqueue(request);
    }

    private void checkFileUploaded() {
        StorageReference listRef = storage.getReference().child("documents/residence_permit/" + uid);
        listRef.listAll()
                .addOnSuccessListener(listResult -> {
                    if (listResult.getItems().size() == 1) {
                        changeButton();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to check documents!", Toast.LENGTH_SHORT).show();
                });
    }

    private void changeButton() {
        changeButtonText();
        buttonDownload.setVisibility(View.VISIBLE);
        check.setVisibility(View.VISIBLE);
    }

    private void changeButtonText() {
        buttonBrowse.setText("Change file");
    }
}