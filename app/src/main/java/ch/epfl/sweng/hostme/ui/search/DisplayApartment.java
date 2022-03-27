package ch.epfl.sweng.hostme.ui.search;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.adapter.ApartmentAdapter;

public class DisplayApartment extends AppCompatActivity {

    private static final String APARTMENTS_PATH = "apartments/";
    private static final String PREVIEW_1_JPG = "/preview1.jpg";
    private StorageReference storageReference;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_apartment);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        ImageView image = findViewById(R.id.apart_image);
        String lid = getIntent().getStringExtra(ApartmentAdapter.LID);

        Intent intent = getIntent();
        String addr = intent.getStringExtra(ApartmentAdapter.ADDR);
        int area = intent.getIntExtra(ApartmentAdapter.AREA, 0);
        int rent = intent.getIntExtra(ApartmentAdapter.RENT, 0);
        String lease = intent.getStringExtra(ApartmentAdapter.LEASE);
        int occupants = intent.getIntExtra(ApartmentAdapter.OCCUPANT, 0);
        String proprietor = intent.getStringExtra(ApartmentAdapter.PROPRIETOR);
        changeText(addr, R.id.addr);
        changeText(String.valueOf(area), R.id.area);
        changeText(String.valueOf(rent), R.id.price);
        changeText(lease, R.id.lease);
        changeText(String.valueOf(occupants), R.id.occupants);
        changeText(proprietor, R.id.proprietor);

        storageReference = FirebaseStorage.getInstance().getReference().child(APARTMENTS_PATH + lid + PREVIEW_1_JPG);
        try {
            final File localFile = File.createTempFile("preview1", "jpg");
            storageReference.getFile(localFile)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            image.setImageBitmap(bitmap);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void changeText(String addr, int p) {
        TextView addrText = findViewById(p);
        addrText.setText(addr);
    }
}
