package ch.epfl.sweng.hostme.messages;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.hostme.R;

public class FcmNotificationsSender {

    private final static String POST_URL = "https://fcm.googleapis.com/fcm/send";
    String userFcmToken;
    String title;
    String body;
    Context context;
    Activity activity;

    public FcmNotificationsSender(String userFcmToken, String title, String body, Context context, Activity activity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.context = context;
        this.activity = activity;
    }

    /**
     * send notifications with title and body
     */
    public void sendNotifications() {
        RequestQueue requestQueue = Volley.newRequestQueue(this.activity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", this.userFcmToken);
            JSONObject notificationObject = new JSONObject();
            notificationObject.put("title", this.title);
            notificationObject.put("content", this.body);
            notificationObject.put("image", R.mipmap.ic_launcher);
            mainObj.put("data", notificationObject);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, POST_URL, mainObj, response -> {
            }, error -> {
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + context.getString(R.string.fcm_server_key));
                    return header;
                }
            };
            requestQueue.add(request);
        } catch (Exception ignored) {
        }
    }

}
