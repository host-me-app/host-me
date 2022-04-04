package ch.epfl.sweng.hostme.wallet;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;

import ch.epfl.sweng.hostme.database.Storage;

public class DocumentDownloader {

    private static final String DOWNLOAD_SUCCEED_MESSAGE = "Download succeed!";
    private static final String DOWNLOAD_FAILED_MESSAGE = "Download failed!";
    private final Document document;
    private final String uid;
    private final Context context;

    public DocumentDownloader(Document document, String uid, Activity activity, Context context) {
        this.document = document;
        this.uid = uid + "/";
        this.context = context;
        Button buttonDownload = activity.findViewById(document.getButtonDownloadId());
        buttonDownload.setOnClickListener(view -> this.downloadFile());
    }

    private void downloadFile() {
        String pathString = this.document.getPath() + this.uid + this.document.getFileName() + this.document.getFileExtension();
        StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);
        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            this.download(uri.toString());
            Toast.makeText(context, DOWNLOAD_SUCCEED_MESSAGE, Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(exception -> Toast.makeText(context, DOWNLOAD_FAILED_MESSAGE, Toast.LENGTH_SHORT).show());
    }

    private void download(String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, this.document.getFileName() + this.document.getFileExtension());
        downloadManager.enqueue(request);
    }
}