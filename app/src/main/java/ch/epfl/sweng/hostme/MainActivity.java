package ch.epfl.sweng.hostme;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        Button logInButt = findViewById(R.id.logInButton);
        logInButt.setOnClickListener(view -> welcome());

        Button signUp = findViewById(R.id.signUpButton);
        signUp.setOnClickListener(view -> askUserQuestion());

    }

    private void welcome() {
        Intent intent = new Intent(this, WelcomePage.class);
        startActivity(intent);
    }

    private void askUserQuestion() {
        Intent intent = new Intent(this, UserCreationPage1.class);
        startActivity(intent);
    }

}
