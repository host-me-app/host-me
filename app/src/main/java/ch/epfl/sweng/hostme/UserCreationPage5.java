package ch.epfl.sweng.hostme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class UserCreationPage5 extends AppCompatActivity {
    public final static Map<String, String> DATA = new HashMap<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_pwd);

        mAuth = FirebaseAuth.getInstance();

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        Button terminateButt = findViewById(R.id.terminateButton);
        EditText pwd = findViewById(R.id.password);
        EditText confirm_pwd = findViewById(R.id.confirm_pwd);

        terminateButt.setOnClickListener(view -> {
            String pwdText = pwd.getText().toString();
            String confirm_pwdText = confirm_pwd.getText().toString();
            if (pwdText.equals(confirm_pwdText) && PasswordValidator.isValid(pwdText)) {
                createUser(DATA.get(UserCreationPage4.MAIL), pwdText);
            }
        });
    }

    private void welcome() {
        Intent intent = new Intent(UserCreationPage5.this, WelcomePage.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                Toast.makeText(UserCreationPage5.this, "Authentication successed.",
                        Toast.LENGTH_SHORT).show();
                welcome();
            } else {
                Toast.makeText(UserCreationPage5.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
