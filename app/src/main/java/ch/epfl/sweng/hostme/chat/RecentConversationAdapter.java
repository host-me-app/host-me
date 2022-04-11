package ch.epfl.sweng.hostme.chat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.epfl.sweng.hostme.databinding.ItemContainerRecentConvoBinding;

public class RecentConversationAdapter extends RecyclerView.Adapter<RecentConversationAdapter.ConversionViewHolder> {

    private final List<ChatMessage> chatMessages;

    public RecentConversationAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                ItemContainerRecentConvoBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        holder.setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder{

        ItemContainerRecentConvoBinding binding;

        ConversionViewHolder(ItemContainerRecentConvoBinding itemContainerRecentConvoBinding){
            super(itemContainerRecentConvoBinding.getRoot());
            binding = itemContainerRecentConvoBinding;
        }

        void setData(ChatMessage chatMessage){
            binding.textName.setText(chatMessage.conversationName);
            binding.textRecentMessage.setText(chatMessage.message);
        }
    }

}
