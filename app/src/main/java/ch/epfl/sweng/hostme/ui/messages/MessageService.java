package ch.epfl.sweng.hostme.ui.messages;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ch.epfl.sweng.hostme.MenuActivity;
import ch.epfl.sweng.hostme.utils.Constants;

public class MessageService extends FirebaseMessagingService {
    NotificationManager mNotificationManager;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(false);
        }

        NotificationCompat.Builder builder = createBuilder(remoteMessage);
        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        mNotificationManager.notify(100, builder.build());

    }

    @NonNull
    private NotificationCompat.Builder createBuilder(RemoteMessage remoteMessage) {
        int resourceImage = getResources().getIdentifier(remoteMessage.getNotification().getIcon(), "drawable", getPackageName());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID");
        builder.setSmallIcon(resourceImage);

        Intent resultIntent = new Intent(this, CallActivity.class);
        if(remoteMessage.getNotification().getTitle().contentEquals("New Message")){
            resultIntent = new Intent(this, MenuActivity.class);
        }
        resultIntent.putExtra(Constants.FROM_NOTIF, true);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        builder.setContentTitle(remoteMessage.getNotification().getTitle());
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setContentIntent(resultPendingIntent);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        return builder;
    }
}
