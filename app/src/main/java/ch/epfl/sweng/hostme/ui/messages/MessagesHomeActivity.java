package ch.epfl.sweng.hostme.ui.messages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.messaging.FirebaseMessaging;

import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.databinding.ActivityMessagesHomeBinding;
import ch.epfl.sweng.hostme.utils.Constants;

public class MessagesHomeActivity extends AppCompatActivity {

    private ActivityMessagesHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessagesHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getToken();
        setListeners();
    }

    private void setListeners() {
        binding.contactButton.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), UsersActivity.class)));
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        DocumentReference documentReference =
                Database.getCollection(Constants.KEY_COLLECTION_USERS).document(Auth.getUid());

        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Unable to update token"));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}