package ch.epfl.sweng.hostme.ui.messages;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;

import java.util.Objects;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Database;
import io.agora.rtc.RtcEngine;

public class JoinCall extends Call {

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA, Manifest.permission.BLUETOOTH_CONNECT};
    private RtcEngine mRtcEngine;
    private ImageView audioButt;
    private ImageView leaveButt;
    private ImageView videoButt;
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
        audioButt = findViewById(R.id.audioBtn);
        audioButt.setOnClickListener(l -> onAudioMuteClicked(audioButt));
        leaveButt = findViewById(R.id.leaveBtn);
        leaveButt.setOnClickListener(l -> onLeaveChannelClicked());
        videoButt = findViewById(R.id.videoBtn);
        videoButt.setOnClickListener(l -> onVideoMuteClicked(videoButt));
        findViewById(R.id.joinBtn).setVisibility(View.GONE);
        joinChannel();
    }
}
