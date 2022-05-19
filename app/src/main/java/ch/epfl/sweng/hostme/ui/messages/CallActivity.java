package ch.epfl.sweng.hostme.ui.messages;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import ch.epfl.sweng.hostme.MenuActivity;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.users.User;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;


public class CallActivity extends AppCompatActivity {

    public static final String FROM_NOTIF = "from_notif";
    private static final int PERMISSION_REQ_ID = 22;
    private final static CollectionReference reference = Database.getCollection(ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_USERS);
    private final static int expirationTimeInSeconds = 3600;
    private final static String CERTIF = "fd11cdf9c54d490c9756be9d087c5293";
    private static String currUserID;
    boolean isFromNotif;
    private RtcEngine mRtcEngine;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        }

        @Override
        public void onUserJoined(final int uid, int elapsed) {
            runOnUiThread(() -> setupRemoteVideoStream(uid));
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            runOnUiThread(() -> removeVideo(R.id.bg_video_container));
        }

        @Override
        public void onRemoteVideoStateChanged(final int uid, final int state, int reason, int elapsed) {
            runOnUiThread(() -> onRemoteUserVideoToggle(state));
        }
    };
    private ImageView audioButt;
    private ImageView leaveButt;
    private ImageView videoButt;
    private ImageView switchCamera;
    private User user;
    private String result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        isFromNotif = getIntent().getBooleanExtra(FROM_NOTIF, false);
        currUserID = Auth.getUid();
        audioButt = findViewById(R.id.audioBtn);
        audioButt.setOnClickListener(l -> onAudioMuteClicked(audioButt));
        leaveButt = findViewById(R.id.leaveBtn);
        leaveButt.setOnClickListener(l -> onLeaveChannelClicked());
        videoButt = findViewById(R.id.videoBtn);
        videoButt.setOnClickListener(l -> onVideoMuteClicked(videoButt));
        switchCamera = findViewById(R.id.switch_camera);
        switchCamera.setOnClickListener(v -> onSwitchCamera());
        user = (User) getIntent().getSerializableExtra("user");
        checkPermissionsAndInitEngine();
    }

    private void onSwitchCamera() {
        mRtcEngine.switchCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRtcEngine.leaveChannel();
        RtcEngine.destroy();
    }

    private void checkPermissionsAndInitEngine() {
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.RECORD_AUDIO);
        permissions.add(Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
        }
        String[] REQUESTED_PERMISSIONS = new String[permissions.size()];
        REQUESTED_PERMISSIONS = permissions.toArray(REQUESTED_PERMISSIONS);
        if (ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED) {
            initAgoraEngine();
        } else {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }
    }

    private void sendNotif() {
        FcmNotificationsSender sender = new FcmNotificationsSender(user.token, "Call",
                "Click to answer", getApplicationContext(), CallActivity.this);
        sender.sendNotifications();
        reference.document(user.id).update("roomName", currUserID);
    }

    public void joinChannel() {
        reference.document(Auth.getUid()).get().addOnSuccessListener(res -> {
            if (res.exists()) {
                String roomName = res.getString("roomName");
                if (roomName != null) {
                    initToken(roomName);
                    mRtcEngine.joinChannel(result, roomName, null, 0);
                    setupLocalVideoFeed();
                } else {
                    Toast.makeText(getApplicationContext(), "Your correspondent is no longer available",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionsAndInitEngine();
            }
        }
    }

    private void initAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(getApplicationContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception ignored) {
        }
        setupSession();
        if (isFromNotif) {
            joinChannel();
        } else {
            onJoinChannelClicked();
            sendNotif();
        }
    }

    private void setupSession() {
        mRtcEngine.setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_COMMUNICATION);
        mRtcEngine.enableVideo();
        mRtcEngine.enableAudio();
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x480, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideoFeed() {
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);

        mRtcEngine.enableVideo();
        VideoEncoderConfiguration mVEC = new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
        mRtcEngine.setVideoEncoderConfiguration(mVEC);
        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        FrameLayout videoContainer = findViewById(R.id.floating_video_container);
        SurfaceView videoSurface = RtcEngine.CreateRendererView(getBaseContext());
        videoSurface.setZOrderMediaOverlay(true);
        videoContainer.addView(videoSurface);
        mRtcEngine.setupLocalVideo(new VideoCanvas(videoSurface, VideoCanvas.RENDER_MODE_FIT, 0));
    }

    private void setupRemoteVideoStream(int uid) {
        FrameLayout videoContainer = findViewById(R.id.bg_video_container);
        SurfaceView videoSurface = RtcEngine.CreateRendererView(getBaseContext());
        videoContainer.addView(videoSurface);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(videoSurface, VideoCanvas.RENDER_MODE_FIT, uid));
        mRtcEngine.setRemoteSubscribeFallbackOption(io.agora.rtc.Constants.STREAM_FALLBACK_OPTION_AUDIO_ONLY);
    }

    private void onRemoteUserVideoToggle(int state) {
        FrameLayout videoContainer = findViewById(R.id.bg_video_container);

        SurfaceView videoSurface = (SurfaceView) videoContainer.getChildAt(0);
        videoSurface.setVisibility(state == 0 ? View.GONE : View.VISIBLE);

    }


    public void onJoinChannelClicked() {
        String channelName = currUserID;
        initToken(channelName);
        mRtcEngine.joinChannel(result, channelName, null, 0);
        setupLocalVideoFeed();
    }

    private void initToken(String channelName) {
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int) (System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        result = token.buildTokenWithUid(getString(R.string.agora_app_id), CERTIF,
                channelName, 0, RtcTokenBuilder.Role.Role_Publisher, timestamp);
    }

    public void onLeaveChannelClicked() {
        mRtcEngine.leaveChannel();
        removeVideo(R.id.floating_video_container);
        removeVideo(R.id.bg_video_container);
        if (!isFromNotif) {
            reference.document(user.id).update("roomName", null);
        }
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        onLeaveChannelClicked();
    }

    private void removeVideo(int containerID) {
        FrameLayout videoContainer = findViewById(containerID);
        videoContainer.setVisibility(View.GONE);
        videoContainer.removeAllViews();
    }

    public void onAudioMuteClicked(View view) {
        ImageView btn = (ImageView) view;
        if (btn.isSelected()) {
            btn.setSelected(false);
            btn.setImageResource(R.drawable.btn_mute);
        } else {
            btn.setSelected(true);
            btn.setImageResource(R.drawable.btn_unmute);
        }
        mRtcEngine.muteLocalAudioStream(btn.isSelected());
    }

    public void onVideoMuteClicked(View view) {
        ImageView btn = (ImageView) view;
        if (btn.isSelected()) {
            btn.setSelected(false);
            btn.setImageResource(R.drawable.video_toggle_btn);
        } else {
            btn.setSelected(true);
            btn.setImageResource(R.drawable.video_toggle_active_btn);
        }
        mRtcEngine.muteLocalVideoStream(btn.isSelected());

        FrameLayout container = findViewById(R.id.floating_video_container);
        container.setVisibility(btn.isSelected() ? View.GONE : View.VISIBLE);
        SurfaceView videoSurface = (SurfaceView) container.getChildAt(0);
        videoSurface.setZOrderMediaOverlay(!btn.isSelected());
        videoSurface.setVisibility(btn.isSelected() ? View.GONE : View.VISIBLE);
    }
}