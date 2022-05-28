package ch.epfl.sweng.hostme.chat;

import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.epfl.sweng.hostme.R;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;
    private final List<ChatMessage> chatMessages;
    private final String senderId;

    public ChatAdapter(List<ChatMessage> chatMessages, String senderId) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_sent_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_received_message, parent, false);
        }
        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MessageViewHolder) holder).setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView textMessage;
        TextView messageDate;


        MessageViewHolder(View itemView) {
            super(itemView);
            this.textMessage = itemView.findViewById(R.id.text_message);
            this.messageDate = itemView.findViewById(R.id.message_date);
        }

        void setData(ChatMessage chatMessage) {
            if (chatMessage.isDocument) {
                this.textMessage.setClickable(true);
                this.textMessage.setMovementMethod(LinkMovementMethod.getInstance());
                String text = "<a href='" + chatMessage.message + "' download> Download " + chatMessage.documentName + "</a>";
                this.textMessage.setText(Html.fromHtml(text));
                this.textMessage.setLinkTextColor(Color.GREEN);
            } else {
                this.textMessage.setText(chatMessage.message);
                this.messageDate.setText(chatMessage.dateTime);
            }
        }
    }
}
