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
        updateFileMetadata(dayOfMonth, month, year);

    }

    private void updateFileMetadata(int dayOfMonth, int month, int year) {
        String pathString = document.getPath() + uid + document.getFileName() + document.getFileExtension();
        StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);
        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
        StorageMetadata metadata = new StorageMetadata.Builder() // Create file metadata including the content type
                .setContentType("document/pdf")
                .setCustomMetadata(KEY_CUSTOM_METADATA_EXPIRATION_DATE, date)
                .build();
        fileRef.updateMetadata(metadata) // Update metadata properties
                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        // Updated metadata is in storageMetadata
                        expDateText.setText(date);
                        notif(year,dayOfMonth, month);
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
    private void notif(int year, int dayOfMonth, int month) {
        clearPreviouslyScheduledNotif();
        createNotificationChannel();
        Calendar datePicked = Calendar.getInstance();
        datePicked.set(year, month, dayOfMonth);
        scheduleNotification(datePicked);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        String name = "Wallet Reminders";
        String desc = "Reminders for the documents of the wallet";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        // CHANNEL_ID must be unique per package
        //Creating an existing notification channel with its original values performs no operation,
        // so it's safe to call many times this code (even when starting an app)
        //If a channel with the same ID exists then Android doesn't create another.
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(desc);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

    }


    private void clearPreviouslyScheduledNotif() {

        int all_reminders_size = RemindersDate.values().length;
        Intent intent = new Intent(context, ReminderNotification.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        for (int i = 0; i < all_reminders_size; ++i) {

            intent.putExtra(notification_ID_Extra, document.ordinal() * all_reminders_size + i);
            String title = "Document expiration reminder";
            String message = "Your " + document.getDocumentName().toLowerCase() + " will expire " + RemindersDate.values()[i].message;

            PendingIntent pend = PendingIntent.getBroadcast(
                    context.getApplicationContext(),
                    document.ordinal() * all_reminders_size + i,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
            );
            alarmManager.cancel(pend);
            pend.cancel();
        }


    }

    private void scheduleNotification(Calendar date) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        ArrayList<Long> scheduledTime = new ArrayList<Long>();
        ArrayList<String> scheduledMessage = new ArrayList<String>();
        modifyScheduledArray(date, scheduledTime, scheduledMessage);

        int all_reminders_size = RemindersDate.values().length;
        for (int i = 0; i < scheduledTime.size(); ++i) {

            Intent intent = new Intent(context, ReminderNotification.class);
            String title = "Document expiration reminder";
            String message = "Your " + document.getDocumentName().toLowerCase() + " will expire " + scheduledMessage.get(i);

            intent.putExtra(notification_ID_Extra, document.ordinal() * all_reminders_size + i); //same document notifications goes to a different notification

            intent.putExtra(titleExtra, title);
            intent.putExtra(messageExtra, message);


            PendingIntent pend = PendingIntent.getBroadcast(
                    context.getApplicationContext(),
                    document.ordinal() * all_reminders_size + i, //PendingIntent a un requestCode different sinon ça override la même PendingIntent.
                    intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
            );

            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    scheduledTime.get(i),
                    pend
            );
        }
    }

    private void modifyScheduledArray(Calendar targetDate, ArrayList<Long> scheduledTime, ArrayList<String> scheduledMessage) {

        for (RemindersDate period : RemindersDate.values()) {
            Calendar targetDate_copy = (Calendar) targetDate.clone();
            targetDate_copy.add(period.timeUnit, period.number);

            Calendar now = Calendar.getInstance();
            if (targetDate_copy.getTimeInMillis() - now.getTimeInMillis() > 0) {
                scheduledTime.add(targetDate_copy.getTimeInMillis());
                scheduledMessage.add(period.message);
            }

        }

        Calendar nowTestDemo = Calendar.getInstance();
        nowTestDemo.add(Calendar.SECOND, 20);
        scheduledTime.add(nowTestDemo.getTimeInMillis());
        scheduledMessage.add("in 2 days");
    }


}
