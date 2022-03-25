package ch.epfl.sweng.hostme.ui.search;

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
        String lid = getIntent().getStringExtra(SearchFragment.LID);

        String addr = getIntent().getStringExtra(SearchFragment.ADDR);
        changeText(addr, R.id.addr);

        int area = getIntent().getIntExtra(SearchFragment.AREA, 0);
        changeText(String.valueOf(area), R.id.area);

        int rent = getIntent().getIntExtra(SearchFragment.RENT, 0);
        changeText(String.valueOf(rent), R.id.price);

        String lease = getIntent().getStringExtra(SearchFragment.LEASE);
        changeText(lease, R.id.lease);

        int occupants = getIntent().getIntExtra(SearchFragment.OCCUPANT, 0);
        changeText(String.valueOf(occupants), R.id.occupants);

        String proprietor = getIntent().getStringExtra(SearchFragment.PROPRIETOR);
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
