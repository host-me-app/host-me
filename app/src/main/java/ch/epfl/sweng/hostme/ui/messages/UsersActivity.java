package ch.epfl.sweng.hostme.ui.messages;

import static ch.epfl.sweng.hostme.utils.Constants.FROM;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_USERS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_EMAIL;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FCM_TOKEN;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FIRSTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_LASTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_USER;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.users.User;
import ch.epfl.sweng.hostme.users.UserListener;
import ch.epfl.sweng.hostme.users.UsersAdapter;

public class UsersActivity extends AppCompatActivity implements UserListener {

    private final static String NO_USER = "No user available";
    private RecyclerView recyclerView;
    private TextView errorMessage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        this.recyclerView = this.findViewById(R.id.users_recycler_view);
        this.errorMessage = this.findViewById(R.id.error_message);
        this.progressBar = this.findViewById(R.id.progress_bar);
        getUsers();
    }

    private void getUsers() {
        loading(true);
        Database.getCollection(KEY_COLLECTION_USERS).get()
        .addOnSuccessListener(result -> {
            loading(false);
            String currentUserId = Auth.getUid();
            ArrayList<User> users = new ArrayList<>();
            for (QueryDocumentSnapshot queryDocumentSnapshot : result) {
                if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                    continue;
                }
                User user = new User();
                user.setName(queryDocumentSnapshot.getString(KEY_FIRSTNAME)
                        + " " + queryDocumentSnapshot.getString(KEY_LASTNAME));
                user.setEmail(queryDocumentSnapshot.getString(KEY_EMAIL));
                user.setToken(queryDocumentSnapshot.getString(KEY_FCM_TOKEN));
                user.setId(queryDocumentSnapshot.getId());
                user.setImage("profilePicture/" + user.getId() + "/profile.jpg");
                users.add(user);
            }
            if (users.size() > 0) {
                this.displayRecycler(users);
            } else {
                showErrorMessage();
            }
        }).addOnFailureListener(error -> showErrorMessage());
    }

    private void displayRecycler(ArrayList<User> users) {
        List<User> usersWithoutDuplicate = new ArrayList<>(new HashSet<>(users));
        UsersAdapter usersAdapter = new UsersAdapter(usersWithoutDuplicate, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setItemViewCacheSize(20);
        this.recyclerView.setDrawingCacheEnabled(true);
        this.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        this.recyclerView.setVisibility(View.VISIBLE);
        this.recyclerView.setAdapter(usersAdapter);
    }


    private void showErrorMessage() {
        this.errorMessage.setText(NO_USER);
        this.errorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            this.progressBar.setVisibility(View.VISIBLE);
        } else {
            this.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(FROM, "");
        intent.putExtra(KEY_USER, user);
        startActivity(intent);
    }

}