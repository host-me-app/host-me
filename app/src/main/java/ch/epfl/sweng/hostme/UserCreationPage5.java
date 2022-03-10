package ch.epfl.sweng.hostme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserCreationPage5 extends AppCompatActivity {
    public final static Map<String, String> DATA = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_pwd);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        Button terminateButt = findViewById(R.id.terminateButton);
        terminateButt.setOnClickListener(view -> welcome());
    }

    private void welcome() {
        Intent intent = new Intent(UserCreationPage5.this, WelcomePage.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }
}
