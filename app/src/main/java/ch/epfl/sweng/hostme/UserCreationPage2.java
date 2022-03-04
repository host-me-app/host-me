package ch.epfl.sweng.hostme;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class UserCreationPage2 extends AppCompatActivity {
    public final static String FIRST_NAME = "firstName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_firstname);
        Objects.requireNonNull(this.getSupportActionBar()).hide();


        EditText userName = findViewById(R.id.firstName);
        Button nameButt = findViewById(R.id.nextButtonFirstName);
        nameButt.setOnClickListener(view -> {
            String name = userName.getText().toString();
            UserCreationPage5.DATA.put(FIRST_NAME, name);
            goToPage3();
        });
    }

    private void goToPage3() {
        Intent intent = new Intent(UserCreationPage2.this, UserCreationPage3.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }
}
