package ch.epfl.sweng.hostme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class UserCreationPage5 extends AppCompatActivity {
    public final static Map<String, String> DATA = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_pwd);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        Button terminateButt = findViewById(R.id.terminateButton);
        EditText pwd = findViewById(R.id.password);
        EditText confirm_pwd = findViewById(R.id.confirm_pwd);

        terminateButt.setOnClickListener(view -> {
            String pwdText = pwd.getText().toString();
            String confirm_pwdText = confirm_pwd.getText().toString();
            if (pwdText.equals(confirm_pwdText) && PasswordValidator.isValid(pwdText)) {
                welcome();
            }
        });
    }

    private void welcome() {
        Intent intent = new Intent(UserCreationPage5.this, WelcomePage.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }
}
