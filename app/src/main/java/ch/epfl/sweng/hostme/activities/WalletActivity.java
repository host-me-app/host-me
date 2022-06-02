package ch.epfl.sweng.hostme.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.wallet.Document;
import ch.epfl.sweng.hostme.wallet.DocumentDownloader;
import ch.epfl.sweng.hostme.wallet.DocumentExpirationDate;
import ch.epfl.sweng.hostme.wallet.DocumentUploader;
import ch.epfl.sweng.hostme.wallet.ExpirationDatePicker;


public class WalletActivity extends AppCompatActivity {
    private List<DocumentUploader> documentUploader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_card);

        String uid = Auth.getUid();
        documentUploader = new ArrayList<>();
        for (Document doc : Document.values()) {
            documentUploader.add(new DocumentUploader(doc, uid, this, this, DocumentExpirationDate.values()[doc.ordinal()]));
            new DocumentDownloader(doc, uid, this, this);
            new ExpirationDatePicker(doc, uid, this, this, DocumentExpirationDate.values()[doc.ordinal()]);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        documentUploader.get(requestCode - 1).onBrowseFileResult(requestCode, resultCode, data);
    }
}