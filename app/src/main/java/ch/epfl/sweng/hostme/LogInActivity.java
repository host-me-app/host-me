package ch.epfl.sweng.hostme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Objects;

import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.userCreation.CreationContainer;
import ch.epfl.sweng.hostme.userCreation.EnterMailChangePwd;

//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.inappmessaging.FirebaseInAppMessaging;

public class LogInActivity extends AppCompatActivity {

    private static final String PREF_USER_NAME = "username";
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
        checkReminders();
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



    @SuppressLint("MissingPermission")
    private void checkReminders() {

//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "123");
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
//

//        FirebaseAnalytics.getInstance(this).logEvent("exampleTrigger", null);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
//        FirebaseInAppMessaging.getInstance().setAutomaticDataCollectionEnabled(true);
        FirebaseAnalytics.getInstance(this).logEvent("main_screen_opened", null);
//        FirebaseInAppMessaging.getInstance().triggerEvent("exampleTrigger");

    }

    /**
     * Go to forgot password fragment
     */
    private void enterMailToChangePwd() {
        Intent intent = new Intent(LogInActivity.this, EnterMailChangePwd.class);
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
     *
     * @param email
     * @param password
     */
    private void loginUser(String email, String password) {
        Auth.loginUserWithEmail(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        setSharedPref(email);
                        welcome();
                        Toast.makeText(LogInActivity.this, "Authentication succeed.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LogInActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Change the sharedPreferences to keep the user logged in
     * @param email
     */
    private void setSharedPref(String email) {
        SharedPreferences.Editor editor = PreferenceManager.
                getDefaultSharedPreferences(this).edit();
        editor.putString(PREF_USER_NAME, email);
        editor.apply();
    }

}
