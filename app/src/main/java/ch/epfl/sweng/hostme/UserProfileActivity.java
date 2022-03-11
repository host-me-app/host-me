package ch.epfl.sweng.hostme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import ch.epfl.sweng.hostme.models.Profile;
import ch.epfl.sweng.hostme.models.ProfileDatabase;


public class UserProfileActivity extends AppCompatActivity {



    ProfileDatabase p = new ProfileDatabase() {
        @Override
        public Profile getUserProfile() throws Exception {
            return new Profile("Steve Gomez","stv@gmail.com", "+41 78 332 332");
        }
    };

    Profile user;

    {
        try {
            user = p.getUserProfile();
        } catch (Exception e) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        EditText editName = findViewById(R.id.userProfileName);
        EditText editEmail = findViewById(R.id.userProfileEmail);
        EditText editPhone = findViewById(R.id.userProfilePhone);

        editName.setText(user.getName());
        editEmail.setText(user.getEmail());
        editPhone.setText(user.getPhone());




    }
}