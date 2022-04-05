package ch.epfl.sweng.hostme.ui.messages;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import ch.epfl.sweng.hostme.databinding.ActivityChatBinding;
import ch.epfl.sweng.hostme.users.User;
import ch.epfl.sweng.hostme.utils.Constants;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
    }

    private void loadReceiverDetails() {
        System.out.println("111");
        User receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        System.out.println("222");
        binding.textName.setText(receiverUser.name);
        System.out.println("333");
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }
}