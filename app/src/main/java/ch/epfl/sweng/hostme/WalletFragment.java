package ch.epfl.sweng.hostme;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;


public class WalletFragment extends Fragment {

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;

    private View root;
    private FirebaseStorage storage;
    private String uid;

    private Button buttonBrowse;
    private Button buttonDownload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.wallet, container, false);

        storage = FirebaseStorage.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid() + "/";
        buttonBrowse = root.findViewById(R.id.button_browse);
        buttonDownload = root.findViewById(R.id.button_download);

        buttonBrowse.setOnClickListener(view -> askPermissionAndBrowseFile());
        buttonDownload.setOnClickListener(view -> {
            try {
                downloadFile();
            } catch (IOException e) {
                Toast.makeText(this.getContext(), "Download failed 3!", Toast.LENGTH_SHORT).show();
            }
        });
        checkFileUploaded();

        return root;
    }

    private void askPermissionAndBrowseFile() {
        int permisson = ActivityCompat.checkSelfPermission(this.getContext(),
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
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.doBrowseFile();
            } else {
                Toast.makeText(this.getContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_RESULT_CODE_FILECHOOSER) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri file = data.getData();
                    StorageReference fileRef = storage.getReference().child("documents/residence_permit/" + uid + "residence_permit.pdf");
                    UploadTask uploadTask = fileRef.putFile(file);
                    uploadTask.addOnFailureListener(exception -> {
                        Toast.makeText(this.getContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
                    }).addOnSuccessListener(taskSnapshot -> {
                        checkFileUploaded();
                        Toast.makeText(this.getContext(), "Upload successed!", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void downloadFile() throws IOException {
        StorageReference strRef = storage.getReference().child("documents/residence_permit/" + uid + "residence_permit.pdf");
        File file = new File(getContext().getFilesDir(), "residence_permit.pdf");

        strRef.getFile(file).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(this.getContext(), "Download successed!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(exception -> {
            Toast.makeText(this.getContext(), "Download failed!", Toast.LENGTH_SHORT).show();
        });
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
            Toast.makeText(this.getContext(), "Failed to check documents!", Toast.LENGTH_SHORT).show();
        });
    }

    private void changeButton() {
        changeButtonText();
        buttonDownload.setVisibility(View.VISIBLE);
    }

    private void changeButtonText() {
        buttonBrowse.setText("Change file");
    }
}