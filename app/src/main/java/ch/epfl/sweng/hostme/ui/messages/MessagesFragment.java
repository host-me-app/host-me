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
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.chat.ChatMessage;
import ch.epfl.sweng.hostme.chat.ConversionListener;
import ch.epfl.sweng.hostme.chat.RecentConversationAdapter;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.databinding.FragmentMessagesBinding;
import ch.epfl.sweng.hostme.users.User;
import ch.epfl.sweng.hostme.utils.Constants;

public class MessagesFragment extends Fragment implements ConversionListener {

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
        listenConversations();
        return root;
    }

    private void init(){
        conversations = new ArrayList<>();
        conversationAdapter = new RecentConversationAdapter(conversations, this);
        binding.conversationRecycler.setAdapter(conversationAdapter);
        DocumentReference documentReference =
                Database.getCollection(Constants.KEY_COLLECTION_USERS).document(Auth.getUid());

        documentReference.update(Constants.KEY_AVAILABLE, 1)
                .addOnFailureListener(e -> showToast("Unable to be available"));
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

    private void listenConversations(){
        Database.getCollection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, Auth.getUid())
                .addSnapshotListener(eventListener);
        Database.getCollection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, Auth.getUid())
                .addSnapshotListener(eventListener);
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
                  if(Auth.getUid().equals(senderId)){
                      chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                      chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                  }else{
                      chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                      chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                  }
                  chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                  chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                  conversations.add(chatMessage);
              } else if (documentChange.getType() == DocumentChange.Type.MODIFIED){
                  for(int i = 0; i < conversations.size(); i++){
                      String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                      String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                      if(conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)){
                          conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                          conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                          break;
                      }
                  }
              }
          }
          Collections.sort(conversations, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
          conversationAdapter.notifyDataSetChanged();
          binding.conversationRecycler.smoothScrollToPosition(0);
          binding.conversationRecycler.setVisibility(View.VISIBLE);
          binding.progressBar.setVisibility(View.GONE);
       }
    };

    @Override
    public void onConversionClicked(User user) {
        Intent intent = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DocumentReference documentReference =
                Database.getCollection(Constants.KEY_COLLECTION_USERS).document(Auth.getUid());

        documentReference.update(Constants.KEY_AVAILABLE, 0)
                .addOnFailureListener(e -> showToast("cannot be unavailable"));
        binding = null;

    }
}