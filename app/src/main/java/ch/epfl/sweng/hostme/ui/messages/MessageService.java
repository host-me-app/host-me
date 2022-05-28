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

import java.util.Objects;

import ch.epfl.sweng.hostme.MenuActivity;
import ch.epfl.sweng.hostme.utils.Constants;

public class MessageService extends FirebaseMessagingService {
    private final static String CHANNEL_NAME = "Channel human readable title";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(false);
        }

        NotificationCompat.Builder builder = createBuilder(remoteMessage);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        notificationManager.notify(100, builder.build());

    }

    @NonNull
    private NotificationCompat.Builder createBuilder(RemoteMessage remoteMessage) {
        int resourceImage = getResources().getIdentifier(remoteMessage.getData().get("image"), "drawable", getPackageName());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID");
        builder.setSmallIcon(resourceImage);

        Intent resultIntent = new Intent(this, CallActivity.class);
        if (Objects.requireNonNull(remoteMessage.getData().get("title")).contentEquals("New Message")) {
            resultIntent = new Intent(this, MenuActivity.class);
        }
        resultIntent.putExtra(Constants.FROM_NOTIFICATION, true);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        builder.setContentTitle(remoteMessage.getData().get("title"));
        builder.setContentText(remoteMessage.getData().get("content"));
        builder.setContentIntent(resultPendingIntent);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("content")));
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        return builder;
    }
}
