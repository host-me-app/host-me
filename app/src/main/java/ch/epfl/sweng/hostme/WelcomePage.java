package ch.epfl.sweng.hostme;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        String userName = FragmentCreationPage5.DATA.get(FragmentCreationPage2.FIRST_NAME);
        String userLastName = FragmentCreationPage5.DATA.get(FragmentCreationPage3.LAST_NAME);
        TextView welcomeMessage = findViewById(R.id.welcomeMessage);
        welcomeMessage.setText(String.format("Welcome \n %s %s", userName, userLastName));
    }
}