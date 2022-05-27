package ch.epfl.sweng.hostme.ui.messages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ch.epfl.sweng.hostme.MenuActivity;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.chat.ChatAdapter;
import ch.epfl.sweng.hostme.chat.ChatMessage;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.databinding.ActivityChatBinding;
import ch.epfl.sweng.hostme.users.User;
import ch.epfl.sweng.hostme.utils.Connection;
import ch.epfl.sweng.hostme.utils.Constants;
import ch.epfl.sweng.hostme.utils.UserManager;
import ch.epfl.sweng.hostme.wallet.Document;

public class ChatActivity extends AppCompatActivity {

    public static final String FROM = "from";
    private static final String TAG = "chatA";
    private ActivityChatBinding binding;
    private User receiverUser;
    private String apartId;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private String conversionId = null;
    private UserManager userManager;
    private String uid;

    private final OnCompleteListener<QuerySnapshot> conversionOnCompleteListener = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversionId = documentSnapshot.getId();
        }
    };

    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.isDocument = documentChange.getDocument().getBoolean(Constants.KEY_IS_DOCUMENT);
                    chatMessage.documentName = documentChange.getDocument().getString(Constants.KEY_DOCUMENT_NAME);
                    chatMessage.dateTime = getReadableDataTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessage.apartId = documentChange.getDocument().getString(Constants.APART_ID);
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
        if (conversionId == null) {
            checkForConversion();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userManager = new UserManager(getApplicationContext());
        uid = Auth.getUid();
        setListeners();
        loadReceiverDetails();
        init();
        listenMessages();
        ImageView shareButt = findViewById(R.id.shareButton);
        ImageView launchButt = findViewById(R.id.launchButt);
        boolean isConnected = Connection.online(this);
        launchButt.setImageResource(isConnected ? R.drawable.video_call : R.drawable.no_video);
        shareButt.setOnClickListener(v -> {
            if (!isConnected) {
                showToast("You have no Internet connection");
            } else {
                chooseDocumentsToShare();
            }
        });
        launchButt.setOnClickListener(v -> {
            if (!isConnected) {
                showToast("You have no Internet connection");
            } else {
                Intent intent = new Intent(this, CallActivity.class);
                intent.putExtra("user", receiverUser);
                startActivity(intent);
            }
        });
    }

    private void init() {
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                uid
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
    }

    private void sendMessage(String messageStr, boolean isDocument, String documentName) {
        if(messageStr.length() != 0 && !messageStr.trim().isEmpty()) {
            HashMap<String, Object> message = new HashMap<>();
            message.put(Constants.KEY_SENDER_ID, uid);
            message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
            message.put(Constants.KEY_MESSAGE, messageStr.trim());
            message.put(Constants.KEY_IS_DOCUMENT, isDocument);
            message.put(Constants.KEY_DOCUMENT_NAME, documentName);
            message.put(Constants.KEY_TIMESTAMP, new Date());
            message.put(Constants.APART_ID, apartId);
            Database.getCollection(Constants.KEY_COLLECTION_CHAT)
                    .add(message)
                    .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
            addConversation(messageStr.trim());
            sendNotification();
        }
    }

    private void addConversation(String messageStr) {
        if (conversionId != null) {
            updateConversion(messageStr);
            binding.inputMessage.setText(null);
        } else {
            HashMap<String, Object> conversion = new HashMap<>();
            conversion.put(Constants.KEY_SENDER_ID, uid);
            conversion.put(Constants.KEY_SENDER_NAME, userManager.getString(Constants.KEY_SENDER_NAME));
            conversion.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
            conversion.put(Constants.KEY_RECEIVER_NAME, receiverUser.name);
            conversion.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
            conversion.put(Constants.KEY_TIMESTAMP, new Date());
            conversion.put(Constants.APART_ID, apartId);
            addConversion(conversion);
        }
    }

    private void listenMessages() {
        Database.getCollection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, uid)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.id)
                .whereEqualTo(Constants.APART_ID, apartId)
                .addSnapshotListener(eventListener);
        Database.getCollection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverUser.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, uid)
                .whereEqualTo(Constants.APART_ID, apartId)
                .addSnapshotListener(eventListener);
    }

    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        apartId = (String) getIntent().getSerializableExtra(Constants.FROM);
        binding.textName.setText(receiverUser.name);
    }

    private void setListeners() {
        binding.sendButt.setOnClickListener(v -> sendMessage(binding.inputMessage.getText().toString(), false, ""));
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra(FROM) != null) {
            startActivity(new Intent(this, MenuActivity.class));
        } else {
            Intent intent = new Intent(this, UsersActivity.class);
            intent.putExtra(FROM, getIntent().getStringExtra(FROM));
            startActivity(intent);
            finish();
        }
    }

    @NonNull
    private String getReadableDataTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void addConversion(HashMap<String, Object> conversion) {
        Database.getCollection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
    }

    private void updateConversion(String message) {
        DocumentReference documentReference =
                Database.getCollection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversionId);
        documentReference.update(
                Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date()
        );
    }

    private void checkForConversion() {
        if (chatMessages.size() != 0) {
            checkForConversionRemotely(
                    uid,
                    receiverUser.id
            );
            checkForConversionRemotely(
                    receiverUser.id,
                    uid
            );
        }
    }

    private void checkForConversionRemotely(String senderId, String receiverId) {
        Database.getCollection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .whereEqualTo(Constants.APART_ID, apartId)
                .get()
                .addOnCompleteListener(conversionOnCompleteListener);
    }

    private void sendNotification() {
        FcmNotificationsSender sender = new FcmNotificationsSender(receiverUser.token, "New Message",
                " From : " + userManager.getString(Constants.KEY_SENDER_NAME), getApplicationContext(), ChatActivity.this);
        sender.sendNotifications();
        binding.inputMessage.setText(null);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void chooseDocumentsToShare() {
        showDocumentsPickDialog();
    }

    private String[] getDocumentsNames() {
        List<String> list = new ArrayList<>();
        for (Document doc : Document.values()) {
            String documentName = doc.getDocumentName();
            list.add(documentName);
        }
        return list.toArray(new String[Document.values().length]);
    }

    private void showDocumentsPickDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Documents you want to share");
        String[] items = getDocumentsNames();
        ArrayList<Document> itemsSelected = new ArrayList<>();
        builder.setMultiChoiceItems(items, null,
                        (dialog, selectedItemId, isSelected) -> {
                            if (isSelected) {
                                itemsSelected.add(Document.values()[selectedItemId]);
                            } else itemsSelected.remove(Document.values()[selectedItemId]);
                        })
                .setPositiveButton("Share", (dialog, id) -> {
                    sendDocuments(itemsSelected);
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                });

        builder.create().show();
    }

    private void sendDocuments(ArrayList<Document> documentsToShare) {
        for(Document doc : documentsToShare) {
            String pathString = doc.getPath() + uid + "/" + doc.getFileName() + doc.getFileExtension();
            StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                sendMessage(uri.toString(), true, doc.getDocumentName());
            }).addOnFailureListener(exception -> Toast.makeText(this, "Failed to share some documents! \n Check in your wallet if documents are correctly uploaded!", Toast.LENGTH_SHORT).show());
        }
    }

}
