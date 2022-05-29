package ch.epfl.sweng.hostme.ui.messages;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
import ch.epfl.sweng.hostme.users.User;
import ch.epfl.sweng.hostme.utils.Constants;
import ch.epfl.sweng.hostme.utils.UserManager;

public class MessagesFragment extends Fragment implements ConversionListener {

    private static final String TOKEN_FAILED = "Unable to update token";
    private View root;
    private List<ChatMessage> conversations;
    private RecentConversationAdapter conversationAdapter;
    private UserManager userManager;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    String apart_id = documentChange.getDocument().getString(Constants.APART_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = senderId;
                    chatMessage.receiverId = receiverId;
                    chatMessage.apartId = apart_id;
                    if (Auth.getUid().equals(senderId)) {
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                    } else {
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                    }
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversations.add(chatMessage);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0; i < conversations.size(); i++) {
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if (conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)) {
                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            conversationAdapter.notifyDataSetChanged();

            if (this.root != null) {
                this.recyclerView.smoothScrollToPosition(0);
                this.recyclerView.setVisibility(View.VISIBLE);
                this.progressBar.setVisibility(View.GONE);
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.root = inflater.inflate(R.layout.fragment_messages, container, false);
        this.userManager = new UserManager(this.requireContext());
        this.recyclerView = this.root.findViewById(R.id.conversation_recycler);
        this.conversations = new ArrayList<>();
        this.conversationAdapter = new RecentConversationAdapter(conversations, this);
        this.recyclerView.setAdapter(conversationAdapter);
        this.progressBar = this.root.findViewById(R.id.progress_bar);
        this.requireActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        ImageButton contactButton = this.root.findViewById(R.id.contact_button);
        contactButton.setOnClickListener(v -> startActivity(new Intent(this.requireContext(), UsersActivity.class)));
        this.getToken();
        this.listenConversations();
        return root;
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        userManager.putString(Constants.KEY_FCM_TOKEN, token);
        DocumentReference documentReference = Database.getCollection(Constants.KEY_COLLECTION_USERS).document(Auth.getUid());
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(error -> Toast.makeText(getApplicationContext(), TOKEN_FAILED, Toast.LENGTH_SHORT).show());
    }

    private void listenConversations() {
        Database.getCollection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, Auth.getUid())
                .addSnapshotListener(eventListener);
        Database.getCollection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, Auth.getUid())
                .addSnapshotListener(eventListener);
    }

    @Override
    public void onConversionClicked(User user, String apartId) {
        Intent intent = new Intent(requireContext(), ChatActivity.class);
        intent.putExtra(Constants.FROM, apartId);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
    }
}