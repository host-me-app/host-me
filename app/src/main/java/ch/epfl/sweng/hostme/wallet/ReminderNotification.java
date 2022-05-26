package ch.epfl.sweng.hostme.wallet;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import ch.epfl.sweng.hostme.R;


public class ReminderNotification extends BroadcastReceiver {

    public static final String CHANNEL_ID = "ChannelReminders";

    public static final String notification_ID_Extra = "notificationExtra";
    public static final String titleExtra = "titleExtra";
    public static final String messageExtra = "messageExtra";

    @Override
    public void onReceive(Context context, Intent intent) {

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_reminder_wallet)
                .setContentTitle(intent.getStringExtra(titleExtra))
                .setContentText(intent.getStringExtra(messageExtra))
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(intent.getIntExtra(notification_ID_Extra, 1), notification);
        //notification_ID_Extra is different otherwise it updates the same notification
    }
}
