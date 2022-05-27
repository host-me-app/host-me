package ch.epfl.sweng.hostme.wallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Storage;

public class DocumentUploader {

    private static final String UPLOAD_SUCCEED_MESSAGE = "Upload succeed";
    private static final String UPLOAD_FAILED_MESSAGE = "Upload failed";
    private static final String CHOOSE_TEXT = "Choose";
    private static final String CHECK_FAILED_MESSAGE = "Failed to check documents";
    private final Document document;
    private final String uid;
    private final Activity activity;
    private final Context context;
    private final String titleChooser;
    private final TextView buttonBrowseText;
    private final ImageButton buttonDownload;
    private final TextView buttonDownloadText;
    private final ImageView checkImage;
    private final TextView expDateDescriptionText;
    private final TextView expDateText;
    private final Button expDatePickButton;

    public DocumentUploader(Document document, String uid, Activity activity, Context context, DocumentExpirationDate expireDate) {
        this.document = document;
        this.uid = uid + "/";
        this.activity = activity;
        this.context = context;
        this.titleChooser = CHOOSE_TEXT + document.getDocumentName();
        ImageButton buttonBrowse = activity.findViewById(document.getButtonBrowseId());
        this.buttonBrowseText = activity.findViewById(document.getButtonBrowseTextId());
        this.buttonDownload = activity.findViewById(document.getButtonDownloadId());
        this.buttonDownloadText = activity.findViewById(document.getButtonDownloadTextId());
        this.checkImage = activity.findViewById(document.getCheckImageId());
        this.expDateDescriptionText = activity.findViewById(expireDate.getDescriptionFieldTextId());
        this.expDateText = activity.findViewById(expireDate.getExpirationDateTextId());
        this.expDatePickButton = activity.findViewById(expireDate.getPickDateButtonId());
        buttonBrowse.setOnClickListener(view -> this.doBrowseFile());
        this.checkFileUploaded();
    }

    private void doBrowseFile() {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType(this.document.getType());
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFileIntent = Intent.createChooser(chooseFileIntent, this.titleChooser);
        this.activity.startActivityForResult(chooseFileIntent, this.document.getCodePermission());
    }

    public void onBrowseFileResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.document.getCodePermission() && resultCode == Activity.RESULT_OK && data != null) {
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

    @SuppressLint("SetTextI18n")
    private void changeButton() {
        this.buttonBrowseText.setText(R.string.change_file);
        this.buttonDownload.setVisibility(View.VISIBLE);
        this.buttonDownloadText.setVisibility(View.VISIBLE);
        this.checkImage.setVisibility(View.VISIBLE);
        this.expDateDescriptionText.setVisibility(View.VISIBLE);
        this.expDateText.setVisibility(View.VISIBLE);
        this.expDateText.setText("None");
        this.expDatePickButton.setVisibility(View.VISIBLE);
    }
}
