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
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
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
                    List<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : result) {
                        if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                            continue;
                        }
                        User user = new User();
                        user.name = queryDocumentSnapshot.getString(KEY_FIRSTNAME)
                                + " " + queryDocumentSnapshot.getString(KEY_LASTNAME);
                        user.email = queryDocumentSnapshot.getString(KEY_EMAIL);
                        user.token = queryDocumentSnapshot.getString(KEY_FCM_TOKEN);
                        user.id = queryDocumentSnapshot.getId();
                        users.add(user);
                    }
                    if (users.size() > 0) {
                        UsersAdapter usersAdapter = new UsersAdapter(users, this);
                        this.recyclerView.setAdapter(usersAdapter);
                        this.recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        showErrorMessage();
                    }
                }).addOnFailureListener(error -> showErrorMessage());
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
        finish();
    }

}