package ch.epfl.sweng.hostme.ui.messages;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;

import java.util.Objects;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.users.User;
import io.agora.rtc.RtcEngine;

public class CallActivity extends Call {

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA, Manifest.permission.BLUETOOTH_CONNECT};
    private ImageView audioButt;
    private ImageView leaveButt;
    private ImageView videoButt;
    private ImageView joinButt;
    private User user;
    private final static String currUserID = Auth.getUid();
    private final static CollectionReference reference = Database.getCollection("users");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            initAgoraEngine();
        }
        joinButt = findViewById(R.id.joinBtn);
        joinButt.setOnClickListener(l -> {
            sendNotif();
            onJoinChannelClicked();
        });
        audioButt = findViewById(R.id.audioBtn);
        audioButt.setVisibility(View.GONE);
        audioButt.setOnClickListener(l -> onAudioMuteClicked(audioButt));
        leaveButt = findViewById(R.id.leaveBtn);
        leaveButt.setVisibility(View.GONE);
        leaveButt.setOnClickListener(l -> onLeaveChannelClicked());
        videoButt = findViewById(R.id.videoBtn);
        videoButt.setVisibility(View.GONE);
        videoButt.setOnClickListener(l -> onVideoMuteClicked(videoButt));
        user = (User) getIntent().getSerializableExtra("user");
    }

    void sendNotif() {
        FcmNotificationsSender sender = new FcmNotificationsSender(user.token,"Call from ",
                "Click to answer", getApplicationContext(), CallActivity.this);
        sender.sendNotifications();
        reference.document(user.id).update("roomName", currUserID);
    }

    void onJoinChannelClicked() {
        String channelName = currUserID;
        mRtcEngine.joinChannel(null, channelName, null, 0);
        setupLocalVideoFeed();
        joinButt.setVisibility(View.GONE);
        audioButt.setVisibility(View.VISIBLE);
        leaveButt.setVisibility(View.VISIBLE);
        videoButt.setVisibility(View.VISIBLE);
    }
}
