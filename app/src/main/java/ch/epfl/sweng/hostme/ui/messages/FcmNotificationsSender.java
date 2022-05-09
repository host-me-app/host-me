package ch.epfl.sweng.hostme.ui.messages;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.utils.Constants;

public class FcmNotificationsSender {

    String userFcmToken;
    String senderId;
    String senderName;
    String senderToken;
    String title;
    String body;
    Context mContext;
    Activity mActivity;
    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey = "AAAAynpb0cU:APA91bE270JfYV-p1HuLo1nq-ENrwmk9ZZ0Qbj-D37jpVc9YGS2N-wWy-bYfmK12xOtsp5XmIiGgV1w7sxJxbeeS5TEDtrfHoztvH7F7pc7dyNwhr6sd-BXtUBWN9JqNtlXAfi1NSmHe";

    public FcmNotificationsSender(String userFcmToken, String senderId, String senderName, String senderToken, String title, String body, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderToken = senderToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public void sendNotifications() {

        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put(Constants.KEY_USER_ID, senderId);
            notiObject.put(Constants.KEY_SENDER_NAME, senderName);
            notiObject.put(Constants.KEY_FCM_TOKEN, senderToken);
            notiObject.put("title", title);
            notiObject.put("body", body);
            notiObject.put("icon", R.mipmap.ic_launcher);
            mainObj.put("notification", notiObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, response -> {

            }, error -> {}) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;
                }
            };
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}

