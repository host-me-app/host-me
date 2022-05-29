package ch.epfl.sweng.hostme.ui.messages;

import static ch.epfl.sweng.hostme.utils.Constants.CALL_PERM_CODE;
import static ch.epfl.sweng.hostme.utils.Constants.FROM_NOTIFICATION;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_USERS;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;

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

    private final static CollectionReference reference = Database.getCollection(KEY_COLLECTION_USERS);
    private final static int expirationTimeInSeconds = 3600;
    private final static String NOTIFICATION_TITLE = "Call";
    private final static String NOTIFICATION_BODY = "Click to answer";
    private final static String ROOM_NAME = "roomName";
    private static String currUserID;
    boolean isFromNotification;
    private RtcEngine mRtcEngine;
    private ImageView audioButt;
    private ImageView videoButt;
    private User user;
    private String result;

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

        @Override
        public void onLeaveChannel(RtcStats stats) {
            onLeaveChannelClicked();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        currUserID = Auth.getUid();
        this.isFromNotification = getIntent().getBooleanExtra(FROM_NOTIFICATION, false);
        this.audioButt = findViewById(R.id.audio_button);
        this.audioButt.setOnClickListener(l -> onAudioMuteClicked(audioButt));
        this.user = (User) getIntent().getSerializableExtra("user");
        this.videoButt = findViewById(R.id.video_button);
        this.videoButt.setOnClickListener(l -> onVideoMuteClicked(videoButt));
        ImageView switchCamera = findViewById(R.id.switch_camera);
        switchCamera.setOnClickListener(v -> onSwitchCamera());
        ImageView leaveButt = findViewById(R.id.leave_button);
        leaveButt.setOnClickListener(l -> onLeaveChannelClicked());
        checkPermissionsAndInitEngine();
    }

    private void onSwitchCamera() {
        this.mRtcEngine.switchCamera();
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
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, CALL_PERM_CODE);
        }
    }

    private void sendNotification() {
        FcmNotificationsSender sender = new FcmNotificationsSender(this.user.token, NOTIFICATION_TITLE,
                NOTIFICATION_BODY, getApplicationContext(), CallActivity.this);
        sender.sendNotifications();
        reference.document(this.user.id).update(ROOM_NAME, currUserID);
    }

    private void joinChannel() {
        reference.document(Auth.getUid()).get().addOnSuccessListener(res -> {
            if (res.exists()) {
                String roomName = res.getString(ROOM_NAME);
                if (roomName != null) {
                    initToken(roomName);
                    this.mRtcEngine.joinChannel(this.result, roomName, null, 0);
                    setupLocalVideoFeed();
                } else {
                    Toast.makeText(getApplicationContext(), "Your correspondent is no longer available", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionsAndInitEngine();
            }
        }
    }

    private void initAgoraEngine() {
        try {
            this.mRtcEngine = RtcEngine.create(getApplicationContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception ignored) {
        }
        setupSession();
        if (this.isFromNotification) {
            joinChannel();
        } else {
            onJoinChannelClicked();
            sendNotification();
        }
    }

    private void setupSession() {
        this.mRtcEngine.setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_COMMUNICATION);
        this.mRtcEngine.enableVideo();
        this.mRtcEngine.enableAudio();
        this.mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x480, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideoFeed() {
        this.mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        this.mRtcEngine.enableVideo();
        VideoEncoderConfiguration mVEC = new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
        this. mRtcEngine.setVideoEncoderConfiguration(mVEC);
        this.mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        FrameLayout videoContainer = findViewById(R.id.floating_video_container);
        SurfaceView videoSurface = RtcEngine.CreateRendererView(getBaseContext());
        videoSurface.setZOrderMediaOverlay(true);
        videoContainer.addView(videoSurface);
        this.mRtcEngine.setupLocalVideo(new VideoCanvas(videoSurface, VideoCanvas.RENDER_MODE_FIT, 0));
    }

    private void setupRemoteVideoStream(int uid) {
        FrameLayout videoContainer = findViewById(R.id.bg_video_container);
        SurfaceView videoSurface = RtcEngine.CreateRendererView(getBaseContext());
        videoContainer.addView(videoSurface);
        this.mRtcEngine.setupRemoteVideo(new VideoCanvas(videoSurface, VideoCanvas.RENDER_MODE_FIT, uid));
        this.mRtcEngine.setRemoteSubscribeFallbackOption(io.agora.rtc.Constants.STREAM_FALLBACK_OPTION_AUDIO_ONLY);
    }

    private void onRemoteUserVideoToggle(int state) {
        FrameLayout videoContainer = findViewById(R.id.bg_video_container);
        SurfaceView videoSurface = (SurfaceView) videoContainer.getChildAt(0);
        videoSurface.setVisibility(state == 0 ? View.GONE : View.VISIBLE);
    }

    private void onJoinChannelClicked() {
        String channelName = currUserID;
        initToken(channelName);
        this. mRtcEngine.joinChannel(this.result, channelName, null, 0);
        setupLocalVideoFeed();
    }

    private void initToken(String channelName) {
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int) (System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        this.result = token.buildTokenWithUid(getString(R.string.agora_app_id), getString(R.string.agora_certificate),
                channelName, 0, RtcTokenBuilder.Role.Role_Publisher, timestamp);
    }

    private void onLeaveChannelClicked() {
        this.mRtcEngine.leaveChannel();
        removeVideo(R.id.floating_video_container);
        removeVideo(R.id.bg_video_container);
        if (!this.isFromNotification) {
            reference.document(user.id).update(ROOM_NAME, null);
        }
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }

    private void removeVideo(int containerID) {
        FrameLayout videoContainer = findViewById(containerID);
        videoContainer.setVisibility(View.GONE);
        videoContainer.removeAllViews();
    }

    private void onAudioMuteClicked(View view) {
        ImageView btn = (ImageView) view;
        changeButtonState(btn, R.drawable.btn_mute, R.drawable.btn_unmute);
        this.mRtcEngine.muteLocalAudioStream(btn.isSelected());
    }

    private void onVideoMuteClicked(View view) {
        ImageView btn = (ImageView) view;
        changeButtonState(btn, R.drawable.video_toggle_active_btn, R.drawable.video_toggle_btn);
        this. mRtcEngine.muteLocalVideoStream(btn.isSelected());
        FrameLayout container = findViewById(R.id.floating_video_container);
        container.setVisibility(btn.isSelected() ? View.GONE : View.VISIBLE);
        SurfaceView videoSurface = (SurfaceView) container.getChildAt(0);
        videoSurface.setZOrderMediaOverlay(!btn.isSelected());
        videoSurface.setVisibility(btn.isSelected() ? View.GONE : View.VISIBLE);
    }

    private void changeButtonState(ImageView btn, int active, int inactive) {
        if (btn.isSelected()) {
            btn.setSelected(false);
            btn.setImageResource(inactive);
        } else {
            btn.setSelected(true);
            btn.setImageResource(active);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRtcEngine.leaveChannel();
        RtcEngine.destroy();
    }

    @Override
    public void onBackPressed() {
        onLeaveChannelClicked();
    }
}