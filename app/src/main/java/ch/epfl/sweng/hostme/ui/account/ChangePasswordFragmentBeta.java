package ch.epfl.sweng.hostme.ui.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.utils.PasswordValidator;


public class ChangePasswordFragmentBeta extends Fragment {

    private EditText editOldPassword;
    private EditText editNewPassword;
    private EditText editConfirmNewPassword;

    private Button terminateButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_passwordBeta, container, false);

        editOldPassword = view.findViewById(R.id.userProfileOldPassword);
        editNewPassword = view.findViewById(R.id.userProfileNewPassword);
        editConfirmNewPassword = view.findViewById(R.id.userProfileConfirmNewPassword);

        terminateButton = view.findViewById(R.id.userProfileChangePsswdTerminate);

        terminateButton.setOnClickListener( v -> {

            String oldPasswordText = editOldPassword.getText().toString();
            String newPasswordText = editNewPassword.getText().toString();
            String confirmNewPasswordText = editConfirmNewPassword.getText().toString();

            if (newPasswordText.equals(confirmNewPasswordText) && PasswordValidator.isValid(confirmNewPasswordText)) {
                changePasswordDB(oldPasswordText,newPasswordText);
            }
    });

        return view;
    }

    private void changePasswordDB(String oldPassword, String newPassword) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email,oldPassword);

        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                user.updatePassword(newPassword).addOnCompleteListener( task1 -> {

                        if(task1.isSuccessful()){
                            Toast.makeText(getActivity(), "Password Successfully Modified",
                                    Toast.LENGTH_SHORT).show();
                            getActivity().getFragmentManager().popBackStack();
                        }else {
                            Toast.makeText(getActivity(), "Password Modification Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                });

            }else{

                Toast.makeText(getActivity(), "Authentication Failed",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

}