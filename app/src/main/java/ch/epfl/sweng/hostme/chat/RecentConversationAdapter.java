package ch.epfl.sweng.hostme.chat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.epfl.sweng.hostme.databinding.ItemContainerRecentConvoBinding;
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
        if(chatMessages != null) {
            return chatMessages.size();
        }
        else{
            return 0;
        }
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder{

        ItemContainerRecentConvoBinding binding;

        ConversionViewHolder(ItemContainerRecentConvoBinding itemContainerRecentConvoBinding){
            super(itemContainerRecentConvoBinding.getRoot());
            binding = itemContainerRecentConvoBinding;
        }

        void setData(@NonNull ChatMessage chatMessage){
            binding.textName.setText(chatMessage.conversionName);
            binding.textRecentMessage.setText(chatMessage.message);
            binding.getRoot().setOnClickListener(v -> {
                User user = new User();
                user.id = chatMessage.conversionId;
                user.name = chatMessage.conversionName;
                conversionListener.onConversionClicked(user);
            });
        }
    }

}
