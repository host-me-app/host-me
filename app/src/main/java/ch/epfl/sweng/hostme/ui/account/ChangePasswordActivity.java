package ch.epfl.sweng.hostme.ui.account;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.utils.PasswordValidator;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText editOldPassword;
    private EditText editNewPassword;
    private EditText editConfirmNewPassword;

    private Button terminateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        editOldPassword = findViewById(R.id.userProfileOldPassword);
        editNewPassword = findViewById(R.id.userProfileNewPassword);
        editConfirmNewPassword = findViewById(R.id.userProfileConfirmNewPassword);

        terminateButton = findViewById(R.id.userProfileChangePsswdTerminate);

        terminateButton.setOnClickListener(v -> {

            String oldPasswordText = editOldPassword.getText().toString();
            String newPasswordText = editNewPassword.getText().toString();
            String confirmNewPasswordText = editConfirmNewPassword.getText().toString();

            if (newPasswordText.equals(confirmNewPasswordText) && PasswordValidator.isValid(confirmNewPasswordText)) {
                changePasswordDB(oldPasswordText, newPasswordText);
            }
        });
    }

    /**
     * Change app authentication password.
     *
     * @param oldPassword Old Password
     * @param newPassword New Password
     */
    private void changePasswordDB(String oldPassword, String newPassword) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                user.updatePassword(newPassword).addOnCompleteListener(task1 -> {

                    if (task1.isSuccessful()) {
                        Toast.makeText(this, "Password Successfully Modified",
                                Toast.LENGTH_SHORT).show();
                        this.getFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(this, "Password Modification Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            } else {

                Toast.makeText(this, "Authentication Failed",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}