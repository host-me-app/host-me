package ch.epfl.sweng.hostme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.userCreation.CreationContainer;
import ch.epfl.sweng.hostme.userCreation.ChangePasswordActivity;

public class LogInActivity extends AppCompatActivity {

    private final static String LOG_EVENT_NAME = "main_screen_opened";
    private final static String AUTH_FAILED = "Authentication failed";

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
        this.checkReminders();
        setContentView(R.layout.login_page);

        userName = findViewById(R.id.user_name);
        pwd = findViewById(R.id.pwd);

        userName.addTextChangedListener(logInTextWatcher);
        pwd.addTextChangedListener(logInTextWatcher);

        logInButt = findViewById(R.id.log_in_button);
        logInButt.setEnabled(false);
        logInButt.setOnClickListener(view -> {
            String mailText = userName.getText().toString();
            String pwdText = pwd.getText().toString();
            loginUser(mailText, pwdText);
        });

        Button signUp = findViewById(R.id.sign_up_button);
        signUp.setOnClickListener(view -> askUserQuestion());

        Button forgotPwd = findViewById(R.id.forgot_password);
        forgotPwd.setOnClickListener(view -> enterMailToChangePwd());
    }

    /**
     * Disable the back press button
     */
    @Override
    public void onBackPressed() {}

    @SuppressLint("MissingPermission")
    private void checkReminders() {
        FirebaseAnalytics.getInstance(this).logEvent(LOG_EVENT_NAME, null);
    }

    /**
     * Go to forgot password fragment
     */
    private void enterMailToChangePwd() {
        Intent intent = new Intent(LogInActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    /**
     * Go to menu activity
     */
    private void welcome() {
        Intent intent = new Intent(LogInActivity.this, MenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    /**
     * Start user account creation fragment
     */
    private void askUserQuestion() {
        Intent intent = new Intent(LogInActivity.this, CreationContainer.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    /**
     * Login the user with email and password
     * Go to main menu if success
     * Display message if failure
     */
    private void loginUser(String email, String password) {
        Auth.loginUserWithEmail(email, password)
        .addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                welcome();
            } else {
                Toast.makeText(LogInActivity.this, AUTH_FAILED,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
