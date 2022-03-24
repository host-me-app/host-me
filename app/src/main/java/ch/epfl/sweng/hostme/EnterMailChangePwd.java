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

import java.util.Objects;

public class EnterMailChangePwd extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mail;
    private Button sendPwd;
    private final TextWatcher sendMailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String mailText = mail.getText().toString().trim();
            sendPwd.setEnabled(EmailValidator.checkPattern(mailText));
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pwd);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        mAuth = FirebaseAuth.getInstance();


        mail = findViewById(R.id.mailForgotPwd);
        mail.addTextChangedListener(sendMailTextWatcher);
        sendPwd = findViewById(R.id.nextButtonMail2);
        sendPwd.setEnabled(false);

        sendPwd.setOnClickListener(v -> {
            String mailText = mail.getText().toString().trim();
            sendMail(mailText);
        });
    }

    /**
     * Send email to change password
     *
     * @param mailText
     */
    private void sendMail(String mailText) {
        mAuth.sendPasswordResetEmail(mailText)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),
                                "Reset password instructions sent to " + mailText, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                mailText + " does not exist", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
