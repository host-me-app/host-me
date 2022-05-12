package ch.epfl.sweng.hostme.wallet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

import ch.epfl.sweng.hostme.database.Storage;

public class ExpirationDatePicker implements DatePickerDialog.OnDateSetListener {

    private static final String KEY_CUSTOM_METADATA_EXPIRATION_DATE = "Expiration date";
    private final Document document;
    private final String uid;
    private final Activity activity;
    private final Context context;
    private final TextView expDateDescriptionText;
    private DocumentExpirationDate expireDate;
    private TextView expDateText;
    private Button expDatePickButton;

    public ExpirationDatePicker(Document document, String uid, Activity activity, Context context, DocumentExpirationDate expireDate) {

        this.document = document;
        this.uid = uid + "/";
        this.activity = activity;
        this.context = context;
        this.expireDate = expireDate;
        this.expDateDescriptionText = activity.findViewById(expireDate.getDescriptionFieldTextId());
        this.expDateText = activity.findViewById(expireDate.getExpirationDateTextId());
        this.expDatePickButton = activity.findViewById(expireDate.getPickDateButtonId());

        expDatePickButton.setOnClickListener(l -> {
            showDatePickerDialog();
        });
        checkExpirationDateAlreadySet();
    }

    private void checkExpirationDateAlreadySet() {

        String pathString = document.getPath() + uid + document.getFileName() + document.getFileExtension();
        StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);

        fileRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                //File found
                String date = storageMetadata.getCustomMetadata(KEY_CUSTOM_METADATA_EXPIRATION_DATE);

                //Check if there is already a field "Expiration date" (which implies there is surely an expiration date)
                if (date != null) {
                    expDateText.setText(date);
                } else {
                    //No field "Expiration date" found
                    String noExpDate = "None";
                    expDateText.setText(noExpDate);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //No File
            }
        });


    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }


    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar today = Calendar.getInstance();
        view.getDayOfMonth();
        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
        updateFileMetadata(date);

    }

    private void updateFileMetadata(String date) {
        String pathString = document.getPath() + uid + document.getFileName() + document.getFileExtension();
        StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);

        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("document/pdf")
                .setCustomMetadata(KEY_CUSTOM_METADATA_EXPIRATION_DATE, date)
                .build();

        // Update metadata properties
        fileRef.updateMetadata(metadata)
                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        // Updated metadata is in storageMetadata
                        expDateText.setText(date);
                        Toast.makeText(context, "Expiration date Update Succeeded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(context, "Uh-oh, an error occurred!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
