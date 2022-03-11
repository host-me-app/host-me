package ch.epfl.sweng.hostme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText pwd;
    private Button logInButt;

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
        logInButt.setOnClickListener(view -> welcome());

        Button signUp = findViewById(R.id.signUpButton);
        signUp.setOnClickListener(view -> askUserQuestion());

        Button forgotPwd = findViewById(R.id.forgotPassword);
        forgotPwd.setOnClickListener(view -> enterMailToChangePwd());

    }

    private TextWatcher logInTextWatcher = new TextWatcher() {
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
        Intent intent = new Intent(this, EnterMailChangePwd.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    private void welcome() {
        Intent intent = new Intent(this, WelcomePage.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    private void askUserQuestion() {
        Intent intent = new Intent(this, UserCreationPage1.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

}
