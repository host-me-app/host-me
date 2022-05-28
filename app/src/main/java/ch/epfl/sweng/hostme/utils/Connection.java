package ch.epfl.sweng.hostme.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection {
    /**
     * Check if the user has Internet connection
     *
     * @return true if user has connection, false otherwise
     */
    @SuppressLint("MissingPermission")
    public static boolean online(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
