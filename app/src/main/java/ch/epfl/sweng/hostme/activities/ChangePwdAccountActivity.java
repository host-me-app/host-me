package ch.epfl.sweng.hostme.activities;

import static ch.epfl.sweng.hostme.utils.Constants.AUTH_FAILED;
import static ch.epfl.sweng.hostme.utils.Constants.MODIFICATION_FAILED;
import static ch.epfl.sweng.hostme.utils.Constants.MODIFICATION_SUCCEED;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.creation.PasswordValidator;
import ch.epfl.sweng.hostme.database.Auth;

public class ChangePwdAccountActivity extends AppCompatActivity {

    private EditText editOldPassword;
    private EditText editNewPassword;
    private EditText editConfirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editOldPassword = findViewById(R.id.user_profile_old_password);
        editNewPassword = findViewById(R.id.user_profile_new_password);
        editConfirmNewPassword = findViewById(R.id.user_profile_confirm_new_password);
        Button terminateButton = findViewById(R.id.user_profile_change_pwd_terminate);
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

        FirebaseUser user = Auth.getCurrentUser();
        final String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(email), oldPassword);

        user.reauthenticate(credential).addOnSuccessListener(result -> user.updatePassword(newPassword).addOnSuccessListener(result2 -> {
            Toast.makeText(this, MODIFICATION_SUCCEED, Toast.LENGTH_SHORT).show();
            this.getFragmentManager().popBackStack();
        }).addOnFailureListener(error2 -> Toast.makeText(this, MODIFICATION_FAILED, Toast.LENGTH_SHORT).show()))
                .addOnFailureListener(error -> Toast.makeText(this, AUTH_FAILED, Toast.LENGTH_SHORT).show());
    }
}