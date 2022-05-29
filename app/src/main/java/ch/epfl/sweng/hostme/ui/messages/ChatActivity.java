package ch.epfl.sweng.hostme.ui.messages;

import static ch.epfl.sweng.hostme.utils.Constants.FROM;
import static ch.epfl.sweng.hostme.utils.Constants.FROM_CONTACT;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_CONVERSATIONS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_LAST_MESSAGE;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_TIMESTAMP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

import ch.epfl.sweng.hostme.MenuActivity;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.chat.ChatAdapter;
import ch.epfl.sweng.hostme.chat.ChatMessage;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.users.User;
import ch.epfl.sweng.hostme.utils.Connection;
import ch.epfl.sweng.hostme.utils.Constants;
import ch.epfl.sweng.hostme.utils.UserManager;
import ch.epfl.sweng.hostme.wallet.Document;

public class ChatActivity extends AppCompatActivity {

    private static final String NO_INTERNET_MESSAGE = "You have no Internet connection";
    private User receiverUser;
    private String apartId;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private String conversionId = null;
    private UserManager userManager;
    private String uid;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private EditText inputMessage;
    private TextView textName;
    private ImageView sendButt;

    private final OnCompleteListener<QuerySnapshot> conversionOnCompleteListener = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            this.conversionId = documentSnapshot.getId();
        }
    };

    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = this.chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.isDocument = documentChange.getDocument().getBoolean(Constants.KEY_IS_DOCUMENT);
                    chatMessage.documentName = documentChange.getDocument().getString(Constants.KEY_DOCUMENT_NAME);
                    chatMessage.dateTime = getReadableDataTime(documentChange.getDocument().getDate(KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(KEY_TIMESTAMP);
                    chatMessage.apartId = documentChange.getDocument().getString(Constants.APART_ID);
                    this.chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0) {
                this.chatAdapter.notifyDataSetChanged();
            } else {
                this.chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                this.recyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            this.recyclerView.setVisibility(View.VISIBLE);
        }
        this.progressBar.setVisibility(View.GONE);
        if (conversionId == null) {
            this.checkForConversion();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userManager = new UserManager(getApplicationContext());
        uid = Auth.getUid();

        this.recyclerView = findViewById(R.id.chat_recycler_view);
        this.progressBar = findViewById(R.id.progress_bar);
        this.inputMessage = findViewById(R.id.input_message);
        this.textName = findViewById(R.id.text_name);
        this.sendButt = findViewById(R.id.send_button);

        this.chatMessages = new ArrayList<>();
        this.chatAdapter = new ChatAdapter(chatMessages, uid);
        this.recyclerView.setAdapter(chatAdapter);
        this.setListeners();
        this.loadReceiverDetails();
        this.listenMessages();

        ImageView shareButt = findViewById(R.id.share_button);
        ImageView launchButt = findViewById(R.id.launch_button);
        boolean isConnected = Connection.online(this);
        launchButt.setImageResource(isConnected ? R.drawable.video_call : R.drawable.no_video);
        shareButt.setOnClickListener(v -> {
            if (!isConnected) {
                this.showToast();
            } else {
                this.chooseDocumentsToShare();
            }
        });
        launchButt.setOnClickListener(v -> {
            if (!isConnected) {
                this.showToast();
            } else {
                Intent intent = new Intent(this, CallActivity.class);
                intent.putExtra("user", receiverUser);
                startActivity(intent);
            }
        });
    }

    private void sendMessage(String messageStr, boolean isDocument, String documentName) {
        if (messageStr.length() != 0 && !messageStr.trim().isEmpty()) {
            HashMap<String, Object> message = new HashMap<>();
            message.put(Constants.KEY_SENDER_ID, uid);
            message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
            message.put(Constants.KEY_MESSAGE, messageStr.trim());
            message.put(Constants.KEY_IS_DOCUMENT, isDocument);
            message.put(Constants.KEY_DOCUMENT_NAME, documentName);
            message.put(KEY_TIMESTAMP, new Date());
            message.put(Constants.APART_ID, apartId);
            Database.getCollection(Constants.KEY_COLLECTION_CHAT).add(message);
            this.addConversation(messageStr.trim());
            this.sendNotification();
        }
    }

    private void addConversation(String messageStr) {
        if (conversionId != null) {
            this.updateConversion(messageStr);
            this.inputMessage.setText(null);
        } else {
            HashMap<String, Object> conversion = new HashMap<>();
            conversion.put(Constants.KEY_SENDER_ID, uid);
            conversion.put(Constants.KEY_SENDER_NAME, userManager.getString(Constants.KEY_SENDER_NAME));
            conversion.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
            conversion.put(Constants.KEY_RECEIVER_NAME, receiverUser.name);
            conversion.put(KEY_LAST_MESSAGE, this.inputMessage.getText().toString());
            conversion.put(KEY_TIMESTAMP, new Date());
            conversion.put(Constants.APART_ID, apartId);
            this.addConversion(conversion);
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
        this.receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        this.apartId = (String) getIntent().getSerializableExtra(FROM_CONTACT);
        this.textName.setText(receiverUser.name);
    }

    private void setListeners() {
        this.sendButt.setOnClickListener(v -> sendMessage(this.inputMessage.getText().toString(), false, ""));
    }

    @NonNull
    private String getReadableDataTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void addConversion(HashMap<String, Object> conversion) {
        Database.getCollection(KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
    }

    private void updateConversion(String message) {
        DocumentReference documentReference =
                Database.getCollection(KEY_COLLECTION_CONVERSATIONS).document(conversionId);
        documentReference.update(KEY_LAST_MESSAGE, message, KEY_TIMESTAMP, new Date());
    }

    private void checkForConversion() {
        if (chatMessages.size() != 0) {
            this.checkForConversionRemotely(uid, receiverUser.id);
            this.checkForConversionRemotely(receiverUser.id, uid);
        }
    }

    private void checkForConversionRemotely(String senderId, String receiverId) {
        Database.getCollection(KEY_COLLECTION_CONVERSATIONS)
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
        this.inputMessage.setText(null);
    }

    private void showToast() {
        Toast.makeText(getApplicationContext(), NO_INTERNET_MESSAGE, Toast.LENGTH_SHORT).show();
    }

    private String[] getDocumentsNames() {
        List<String> list = new ArrayList<>();
        for (Document doc : Document.values()) {
            String documentName = doc.getDocumentName();
            list.add(documentName);
        }
        return list.toArray(new String[Document.values().length]);
    }

    private void chooseDocumentsToShare() {
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
                .setPositiveButton("Share", (dialog, id) -> sendDocuments(itemsSelected))
                .setNegativeButton("Cancel", (dialog, id) -> {
                });
        builder.create().show();
    }

    private void sendDocuments(ArrayList<Document> documentsToShare) {
        for (Document doc : documentsToShare) {
            String pathString = doc.getPath() + uid + "/" + doc.getFileName() + doc.getFileExtension();
            StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> sendMessage(uri.toString(), true, doc.getDocumentName())).addOnFailureListener(exception -> Toast.makeText(this, "Failed to share some documents! \n Check in your wallet if documents are correctly uploaded!", Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra(FROM_CONTACT) != null) {
            startActivity(new Intent(this, MenuActivity.class));
        } else if (getIntent().getStringExtra(FROM) != null) {
            super.onBackPressed();
        }
    }

}
