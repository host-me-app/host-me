package ch.epfl.sweng.hostme.wallet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;


public class WalletActivity extends AppCompatActivity {
    private List<DocumentUploader> documentUploader;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        String uid = Auth.getUid();
        documentUploader = new ArrayList<>();
        for (Document doc : Document.values()) {
            documentUploader.add(new DocumentUploader(doc, uid, this, this, DocumentExpirationDate.values()[doc.ordinal()]));
            new DocumentDownloader(doc, uid, this, this);
            new ExpirationDatePicker(doc, uid, this, this, DocumentExpirationDate.values()[doc.ordinal()]);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        documentUploader.get(requestCode - 1).onPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        documentUploader.get(requestCode - 1).onBrowseFileResult(requestCode, resultCode, data);
    }
}