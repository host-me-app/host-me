package ch.epfl.sweng.hostme.users;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Storage;

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
        User user = users.get(position);
        holder.textName.setText(user.getName());
        holder.textEmail.setText(user.getEmail());
        setUserData(user, holder);
    }

    /**
     * Set the user image in the storage
     * @param user to be set
     * @param holder view holder of the user
     */
    void setUserData(@NonNull User user, UserViewHolder holder) {
        StorageReference fileRef = Storage.getStorageReferenceByChild(user.getImage());
        try {
            final File localFile = File.createTempFile("profile", "jpg");
            fileRef.getFile(localFile)
                    .addOnSuccessListener(result -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        holder.imageView.setImageBitmap(bitmap);
                    });
        } catch (Exception ignored) {
        }
        holder.itemView.setOnClickListener(v -> userListener.onUserClicked(user));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView textName;
        TextView textEmail;
        ImageView imageView;

        UserViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.textName = itemView.findViewById(R.id.text_name);
            this.textEmail = itemView.findViewById(R.id.text_email);
            this.imageView = itemView.findViewById(R.id.image_profile);
        }

    }
}
