package ch.epfl.sweng.hostme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText userName;
    private EditText pwd;
    private Button logInButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        mAuth = FirebaseAuth.getInstance();

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        userName = findViewById(R.id.userName);
        pwd = findViewById(R.id.pwd);

        userName.addTextChangedListener(logInTextWatcher);
        pwd.addTextChangedListener(logInTextWatcher);

        logInButt = findViewById(R.id.logInButton);
        logInButt.setEnabled(false);
        logInButt.setOnClickListener(view -> {
            String mailText = userName.getText().toString();
            String pwdText = pwd.getText().toString();
            loginUser(mailText, pwdText);
        });

        Button signUp = findViewById(R.id.signUpButton);
        signUp.setOnClickListener(view -> askUserQuestion());

        Button forgotPwd = findViewById(R.id.forgotPassword);
        forgotPwd.setOnClickListener(view -> enterMailToChangePwd());
    }

    private final TextWatcher logInTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String userNameText = userName.getText().toString().trim();
            String pwdText = pwd.getText().toString().trim();

            logInButt.setEnabled(!userNameText.isEmpty() && !pwdText.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void enterMailToChangePwd() {
        Intent intent = new Intent(MainActivity.this, EnterMailChangePwd.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    private void welcome() {
        Intent intent = new Intent(MainActivity.this, WelcomePage.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    private void askUserQuestion() {
        Intent intent = new Intent(MainActivity.this, CreationContainer.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                welcome();
                Toast.makeText(MainActivity.this, "Authentication successed.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
