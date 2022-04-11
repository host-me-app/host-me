package ch.epfl.sweng.hostme.ui.messages;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.chat.ChatMessage;
import ch.epfl.sweng.hostme.chat.RecentConversationAdapter;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.databinding.FragmentMessagesBinding;
import ch.epfl.sweng.hostme.utils.Constants;

public class MessagesFragment extends Fragment {

    private FragmentMessagesBinding binding;
    private List<ChatMessage> conversations;
    private RecentConversationAdapter conversationAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMessagesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        ImageButton contactButt = binding.contactButton;
        contactButt.setOnClickListener(v -> {
            startActivity(new Intent(getActivity().getApplicationContext(), UsersActivity.class));
        });
        getToken();
        return root;
    }

    private void init(){
        conversations = new ArrayList<>();
        conversationAdapter = new RecentConversationAdapter(conversations);
        binding.conversationRecycler.setAdapter(conversationAdapter);
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

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
       if(error != null){
           return;
       }
       if(value != null){
          for(DocumentChange documentChange : value.getDocumentChanges()){
              if(documentChange.getType() == DocumentChange.Type.ADDED){
                  String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                  String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                  ChatMessage chatMessage = new ChatMessage();
                  chatMessage.senderId = senderId;
                  chatMessage.receiverId = receiverId;
              }
          }
       }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}