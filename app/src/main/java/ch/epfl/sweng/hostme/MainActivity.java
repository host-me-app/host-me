package ch.epfl.sweng.hostme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.userCreation.CreationContainer;
import ch.epfl.sweng.hostme.userCreation.EnterMailChangePwd;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText pwd;
    private Button logInButt;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

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

    /**
     * Go to forgot password fragment
     */
    private void enterMailToChangePwd() {
        Intent intent = new Intent(MainActivity.this, EnterMailChangePwd.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    /**
     * Go to menu activity
     */
    private void welcome() {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    /**
     * Start user account creation fragment
     */
    private void askUserQuestion() {
        Intent intent = new Intent(MainActivity.this, CreationContainer.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    /**
     * Login the user with email and password
     * Go to main menu if success
     * Display message if failure
     *
     * @param email
     * @param password
     */
    private void loginUser(String email, String password) {
        Auth.loginUserWithEmail(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        welcome();
                        Toast.makeText(MainActivity.this, "Authentication succeed.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
