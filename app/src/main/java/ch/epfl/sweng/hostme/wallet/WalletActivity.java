package ch.epfl.sweng.hostme.wallet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import ch.epfl.sweng.hostme.R;


public class WalletActivity extends AppCompatActivity {
    private DocumentUploader documentUploader;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        String uid = FirebaseAuth.getInstance().getUid();

        documentUploader = new DocumentUploader(Document.RESIDENCE_PERMIT, uid, this, this);
        new DocumentDownloader(Document.RESIDENCE_PERMIT, uid, this, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        documentUploader.onPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        documentUploader.onBrowseFileResult(requestCode, resultCode, data);
    }
}