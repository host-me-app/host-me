package ch.epfl.sweng.hostme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class UserCreationPage3 extends AppCompatActivity {
    public final static String LAST_NAME = "lastName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_lastname);
        Objects.requireNonNull(this.getSupportActionBar()).hide();


        EditText lastName = findViewById(R.id.lastName);

        Button nameButt = findViewById(R.id.nextButtonLastName);
        nameButt.setOnClickListener(view -> {
            String userLastName = lastName.getText().toString();
            UserCreationPage5.DATA.put(LAST_NAME, userLastName);
            goToPage4();
        });
    }

    private void goToPage4() {
        Intent intent = new Intent(UserCreationPage3.this, UserCreationPage4.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }
}
