package ch.epfl.sweng.hostme.ui.search;

import static ch.epfl.sweng.hostme.utils.Constants.ADDR;
import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS_PATH;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.LEASE;
import static ch.epfl.sweng.hostme.utils.Constants.LID;
import static ch.epfl.sweng.hostme.utils.Constants.OCCUPANT;
import static ch.epfl.sweng.hostme.utils.Constants.PREVIEW_1_JPG;
import static ch.epfl.sweng.hostme.utils.Constants.PROPRIETOR;
import static ch.epfl.sweng.hostme.utils.Constants.RENT;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import ch.epfl.sweng.hostme.R;

public class DisplayApartment extends AppCompatActivity {

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_apartment);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        ImageView image = findViewById(R.id.apart_image);
        String lid = getIntent().getStringExtra(LID);

        Intent intent = getIntent();
        String addr = intent.getStringExtra(ADDR);
        int area = intent.getIntExtra(AREA, 0);
        int rent = intent.getIntExtra(RENT, 0);
        String lease = intent.getStringExtra(LEASE);
        int occupants = intent.getIntExtra(OCCUPANT, 0);
        String proprietor = intent.getStringExtra(PROPRIETOR);
        changeText(addr, R.id.addr);
        changeText(String.valueOf(area), R.id.area);
        changeText(String.valueOf(rent), R.id.price);
        changeText(lease, R.id.lease);
        changeText(String.valueOf(occupants), R.id.occupants);
        changeText(proprietor, R.id.proprietor);

        displayImage(image, lid);

    }

    /**
     * display the image into the ImageView image
     * @param image
     * @param lid
     */
    private void displayImage(ImageView image, String lid) {
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

    /**
     * change the text view to display the data
     * @param addr
     * @param id
     */
    private void changeText(String addr, int id) {
        TextView addrText = findViewById(id);
        addrText.setText(addr);
    }
}
