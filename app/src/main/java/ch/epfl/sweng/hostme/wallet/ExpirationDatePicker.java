package ch.epfl.sweng.hostme.wallet;


import static android.content.Context.NOTIFICATION_SERVICE;
import static ch.epfl.sweng.hostme.wallet.ReminderNotification.CHANNEL_ID;
import static ch.epfl.sweng.hostme.wallet.ReminderNotification.messageExtra;
import static ch.epfl.sweng.hostme.wallet.ReminderNotification.notification_ID_Extra;
import static ch.epfl.sweng.hostme.wallet.ReminderNotification.titleExtra;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

import ch.epfl.sweng.hostme.database.Storage;

public class ExpirationDatePicker implements DatePickerDialog.OnDateSetListener {

    private static final String KEY_CUSTOM_METADATA_EXPIRATION_DATE = "Expiration date";
    private static final int PENDING_ID = 1;
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
        updateFileMetadata(date, dayOfMonth, month, year);

    }

    private void updateFileMetadata(String date, int dayOfMonth, int month, int year) {
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
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        // Updated metadata is in storageMetadata
                        expDateText.setText(date);
                        createNotificationChannel();
                        scheduleNotification(dayOfMonth, month, year);
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        String name = "Wallet Reminders";
        String desc = "Reminders for the documents of the wallet";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        //Creating an existing notification channel with its original values performs no operation,
        // so it's safe to call many times this code (even when starting an app)
        //If a channel with the same ID exists then Android doesn't create another.
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(desc);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

    }


    private void scheduleNotification(int dayOfMonth, int month, int year) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        ArrayList<Long> scheduleTime = getTime(dayOfMonth, month, year);
        for (int i = 0; i < 1; ++i) {

            Intent intent = new Intent(context, ReminderNotification.class);
            String title = "Wallet document expiration reminder";
            String message = "One of your document will soon expire soon";

            if (i == 0) {
                intent.putExtra(notification_ID_Extra, 1);
            }
//            if (i==1) {
//                intent.putExtra(notification_ID_Extra, 2);
//            }


            intent.putExtra(titleExtra, title);
            intent.putExtra(messageExtra, message);


            PendingIntent pend = PendingIntent.getBroadcast(
                    context.getApplicationContext(),
                    PENDING_ID + i, //PendingIntent a un requestCode different sinon ça override la même PendingIntent.
                    intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
            );

            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    scheduleTime.get(i),
                    pend
            );
        }
    }

    private ArrayList<Long> getTime(int dayOfMonth, int month, int year) {

        ArrayList<Long> scheduleTime = new ArrayList<Long>();

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 1);
        scheduleTime.add(now.getTimeInMillis());

        Calendar now2 = Calendar.getInstance();
        now2.add(Calendar.MINUTE, 2);
        scheduleTime.add(now2.getTimeInMillis());

        return scheduleTime;

    }


}
