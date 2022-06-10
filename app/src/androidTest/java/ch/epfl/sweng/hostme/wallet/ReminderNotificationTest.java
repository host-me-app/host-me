package ch.epfl.sweng.hostme.wallet;

import static ch.epfl.sweng.hostme.wallet.ReminderNotification.messageExtra;
import static ch.epfl.sweng.hostme.wallet.ReminderNotification.titleExtra;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

@RunWith(AndroidJUnit4.class)
public class ReminderNotificationTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void notificationIsDisplayed() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, ReminderNotification.class);
        String title = "Wallet document expiration reminder";
        String message = "One of your document will soon expire soon";
        intent.putExtra(titleExtra, title);
        intent.putExtra(messageExtra, message);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pend = PendingIntent.getBroadcast(context.getApplicationContext(), 500, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), pend);
    }

}


