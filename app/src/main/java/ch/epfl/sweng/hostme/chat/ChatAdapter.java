package ch.epfl.sweng.hostme.chat;

import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.epfl.sweng.hostme.databinding.ItemContainerReceivedMessageBinding;
import ch.epfl.sweng.hostme.databinding.ItemContainerSentMessageBinding;

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
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else {
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        } else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position));
        }
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

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(ChatMessage chatMessage) {
            if (chatMessage.isDocument) {
                binding.textMessage.setClickable(true);
                binding.textMessage.setMovementMethod(LinkMovementMethod.getInstance());
                String text = "<a href='" + chatMessage.message + "' download> Download " + chatMessage.documentName + "</a>";
                binding.textMessage.setText(Html.fromHtml(text));
                binding.textMessage.setLinkTextColor(Color.GREEN);
            } else {
                binding.textMessage.setText(chatMessage.message);
                binding.messageDate.setText(chatMessage.dateTime);
            }
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        void setData(ChatMessage chatMessage) {
            if (chatMessage.isDocument) {
                binding.textMessage.setClickable(true);
                binding.textMessage.setMovementMethod(LinkMovementMethod.getInstance());
                String text = "<a href='" + chatMessage.message + "' download> Download " + chatMessage.documentName + "</a>";
                binding.textMessage.setText(Html.fromHtml(text));
                binding.textMessage.setLinkTextColor(Color.GREEN);
            } else {
                binding.textMessage.setText(chatMessage.message);
                binding.messageDate.setText(chatMessage.dateTime);
            }
        }
    }
}
