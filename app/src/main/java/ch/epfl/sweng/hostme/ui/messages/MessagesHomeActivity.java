package ch.epfl.sweng.hostme.ui.messages;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

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
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(FirebaseAuth.getInstance().getUid());

        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(unused -> showToast("Token updated successfully"))
                .addOnFailureListener(e -> showToast("Unable to update token"));
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}