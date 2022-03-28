package ch.epfl.sweng.hostme.ui.messages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ch.epfl.sweng.hostme.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}