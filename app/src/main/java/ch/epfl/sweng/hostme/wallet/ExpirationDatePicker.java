package ch.epfl.sweng.hostme.wallet;


import static android.content.Context.NOTIFICATION_SERVICE;
import static ch.epfl.sweng.hostme.wallet.ReminderNotification.CHANNEL_ID;
import static ch.epfl.sweng.hostme.wallet.ReminderNotification.messageExtra;
import static ch.epfl.sweng.hostme.wallet.ReminderNotification.notification_ID_Extra;
import static ch.epfl.sweng.hostme.wallet.ReminderNotification.titleExtra;

import android.annotation.SuppressLint;
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

import androidx.annotation.RequiresApi;

import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

import ch.epfl.sweng.hostme.database.Storage;

public class ExpirationDatePicker implements DatePickerDialog.OnDateSetListener {

    private static final String KEY_CUSTOM_METADATA_EXPIRATION_DATE = "Expiration date";
    private static final String NOTIFICATION_TITLE = "Document expiration reminder";
    private static final String CHANNEL_NAME = "Wallet Reminders";
    private static final String CHANNEL_DESCRIPTION = "Reminders for the documents of the wallet";
    private static final String EXPIRATION_SUCCEED = "Expiration date update succeeded";
    private static final String EXPIRATION_FAILED = "An error occurred!";
    private final Document document;
    private final String uid;
    private final Context context;
    private final TextView expDateText;


    public ExpirationDatePicker(Document document, String uid, Activity activity, Context context, DocumentExpirationDate expireDate) {
        this.document = document;
        this.uid = uid + "/";
        this.context = context;
        this.expDateText = activity.findViewById(expireDate.getExpirationDateTextId());
        Button expDatePickButton = activity.findViewById(expireDate.getPickDateButtonId());
        expDatePickButton.setOnClickListener(l -> this.showDatePickerDialog());
        this.checkExpirationDateAlreadySet();
    }

    /**
     * check if an expiration date is selected and display it
     */
    private void checkExpirationDateAlreadySet() {
        String pathString = document.getPath() + uid + document.getFileName() + document.getFileExtension();
        StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);

        fileRef.getMetadata().addOnSuccessListener(storageMetadata -> {
            String date = storageMetadata.getCustomMetadata(KEY_CUSTOM_METADATA_EXPIRATION_DATE);
            if (date != null) {
                expDateText.setText(date);
            } else {
                String noExpDate = "None";
                expDateText.setText(noExpDate);
            }
        });
    }

    /**
     * show dialog to the user to select an expiration date
     */
    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        updateFileMetadata(dayOfMonth, month, year);
    }

    /**
     * update file metadata with expiration date
     * @param dayOfMonth day of the date
     * @param month month of the date
     * @param year year of the date
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateFileMetadata(int dayOfMonth, int month, int year) {
        String pathString = document.getPath() + uid + document.getFileName() + document.getFileExtension();
        StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);
        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType(document.getType())
                .setCustomMetadata(KEY_CUSTOM_METADATA_EXPIRATION_DATE, date)
                .build();
        fileRef.updateMetadata(metadata)
                .addOnSuccessListener(storageMetadata -> {
                    expDateText.setText(date);
                    notification(year, dayOfMonth, month);
                    Toast.makeText(context, EXPIRATION_SUCCEED, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception -> Toast.makeText(context, EXPIRATION_FAILED, Toast.LENGTH_SHORT).show());
    }

    /**
     * create notification for a specific date
     * @param year year of the date
     * @param dayOfMonth day of the date
     * @param month month of the date
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notification(int year, int dayOfMonth, int month) {
        clearPreviouslyScheduledNotification();
        createNotificationChannel();
        Calendar datePicked = Calendar.getInstance();
        datePicked.set(year, month, dayOfMonth);
        scheduleNotification(datePicked);
    }

    /**
     * create notification channel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
        channel.setDescription(CHANNEL_DESCRIPTION);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * clear previously scheduled notifications
     */
    private void clearPreviouslyScheduledNotification() {
        int allRemindersSize = RemindersDate.values().length;
        Intent intent = new Intent(context, ReminderNotification.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for (int i = 0; i < allRemindersSize; ++i) {
            intent.putExtra(notification_ID_Extra, document.ordinal() * allRemindersSize + i);
            PendingIntent pend = PendingIntent.getBroadcast(
                    context.getApplicationContext(),
                    document.ordinal() * allRemindersSize + i,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pend);
            pend.cancel();
        }
    }

    /**
     * schedule new notification for the given date
     * @param date date selected
     */
    @SuppressLint("MissingPermission")
    private void scheduleNotification(Calendar date) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        ArrayList<Long> scheduledTime = new ArrayList<>();
        ArrayList<String> scheduledMessage = new ArrayList<>();
        modifyScheduledArray(date, scheduledTime, scheduledMessage);

        int allRemindersSize = RemindersDate.values().length;
        for (int i = 0; i < scheduledTime.size(); ++i) {
            Intent intent = new Intent(context, ReminderNotification.class);
            String message = "Your " + document.getDocumentName().toLowerCase() + " will expire " + scheduledMessage.get(i);
            intent.putExtra(notification_ID_Extra, document.ordinal() * allRemindersSize + i);
            intent.putExtra(titleExtra, NOTIFICATION_TITLE);
            intent.putExtra(messageExtra, message);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context.getApplicationContext(),
                    document.ordinal() * allRemindersSize + i,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
            );
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    scheduledTime.get(i),
                    pendingIntent
            );
        }
    }

    /**
     * modify scheduled dates
     * @param targetDate target date
     * @param scheduledTime scheduled time
     * @param scheduledMessage scheduled message
     */
    private void modifyScheduledArray(Calendar targetDate, ArrayList<Long> scheduledTime, ArrayList<String> scheduledMessage) {
        for (RemindersDate period : RemindersDate.values()) {
            Calendar targetDateCopy = (Calendar) targetDate.clone();
            targetDateCopy.add(period.timeUnit, period.number);

            Calendar now = Calendar.getInstance();
            if (targetDateCopy.getTimeInMillis() - now.getTimeInMillis() > 0) {
                scheduledTime.add(targetDateCopy.getTimeInMillis());
                scheduledMessage.add(period.message);
            }
        }
    }

}
