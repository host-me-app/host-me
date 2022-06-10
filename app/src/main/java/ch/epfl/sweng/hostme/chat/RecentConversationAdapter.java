package ch.epfl.sweng.hostme.chat;

import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_USERS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FCM_TOKEN;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.users.User;

public class RecentConversationAdapter extends RecyclerView.Adapter<RecentConversationAdapter.ConversionViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final ConversionListener conversionListener;

    public RecentConversationAdapter(List<ChatMessage> chatMessages, ConversionListener conversionListener) {
        this.chatMessages = chatMessages;
        this.conversionListener = conversionListener;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_recent_convo, parent, false);
        return new ConversionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        holder.setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView textName;
        TextView textRecentMessage;
        ImageView imageProfile;

        ConversionViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.textName = itemView.findViewById(R.id.text_name);
            this.textRecentMessage = itemView.findViewById(R.id.text_recent_message);
            this.imageProfile = itemView.findViewById(R.id.image_profile);
        }

        /**
         * set the data of the conversation
         *
         * @param chatMessage message of the user
         */
        void setData(@NonNull ChatMessage chatMessage) {
            this.textName.setText(chatMessage.conversionName);
            this.textRecentMessage.setText(chatMessage.message);
            StorageReference fileRef = Storage.getStorageReferenceByChild(chatMessage.image);
            try {
                final File localFile = File.createTempFile("profile", "jpg");
                fileRef.getFile(localFile)
                        .addOnSuccessListener(result -> {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            this.imageProfile.setImageBitmap(bitmap);
                        });
            } catch (Exception ignored) {
            }
            DocumentReference docRef =
                    Database.getCollection(KEY_COLLECTION_USERS).document(chatMessage.conversionId);
            docRef.get().addOnSuccessListener(result -> this.itemView.setOnClickListener(v -> {
                User user = new User();
                user.setId(chatMessage.conversionId);
                user.setName(chatMessage.conversionName);
                user.setToken(result.getString(KEY_FCM_TOKEN));
                conversionListener.onConversionClicked(user, chatMessage.apartId);
            }));
        }
    }

}
