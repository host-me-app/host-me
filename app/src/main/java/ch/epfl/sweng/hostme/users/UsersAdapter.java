package ch.epfl.sweng.hostme.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.epfl.sweng.hostme.R;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<User> users;
    private final UserListener userListener;

    public UsersAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView textName;
        TextView textEmail;

        UserViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.textName = itemView.findViewById(R.id.text_name);
            this.textEmail = itemView.findViewById(R.id.text_email);
        }

        void setUserData(@NonNull User user) {
            this.textName.setText(user.name);
            this.textEmail.setText(user.email);
            this.itemView.setOnClickListener(v -> userListener.onUserClicked(user));
        }
    }
}
