package ch.epfl.sweng.hostme.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import ch.epfl.sweng.hostme.MainActivity;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.databinding.FragmentAccountBinding;
import ch.epfl.sweng.hostme.utils.EmailValidator;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private FirebaseUser user;

    private EditText editName;
    private EditText editEmail;
    private EditText editPhoneNumber;
    private Button saveButton;
    private Button logOutButton;

    private String databaseName;
    private String databaseEmail;
    private String databasePhoneNumber;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        user = FirebaseAuth.getInstance().getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        databaseName = user.getDisplayName();
        databaseEmail = user.getEmail();
        String checkPhone = user.getPhoneNumber();
        if (checkPhone == null){
            databasePhoneNumber = "";
        }
        else{
            databasePhoneNumber = checkPhone;
        }




        editName = view.findViewById(R.id.userProfileName);
        editEmail = view.findViewById(R.id.userProfileEmail);
        editPhoneNumber = view.findViewById(R.id.userProfilePhone);
        logOutButton = view.findViewById(R.id.userProfilelogOutButton);

        saveButton = view.findViewById(R.id.userProfileSaveButton);
        saveButton.setEnabled(false);

        editName.setText(databaseName);
        editEmail.setText(databaseEmail);
        editPhoneNumber.setText(databasePhoneNumber);

        editName.addTextChangedListener(SaveProfileWatcher);
        editEmail.addTextChangedListener(SaveProfileWatcher);


        saveButton.setOnClickListener(v -> {
            String nameText = editName.getText().toString().trim();
            String emailText = editEmail.getText().toString().trim();
            String phoneNumberText = editPhoneNumber.getText().toString().trim();

            if (EmailValidator.checkPattern(emailText)){
                saveUserProperties(nameText, emailText,phoneNumberText);
            }
            saveButton.setEnabled(false);

        });

        logOutButton.setOnClickListener(v -> {
            logUserOut();
        });

        return view;
    }


    /**
     * Logs the user out of the app.
     */
    private void logUserOut() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }


    /**
     * Save new properties of the user on the app
     *
     * @param nameText new name
     * @param emailText new email
     * @param phoneNumberText new Phone Number
     */
    private void saveUserProperties(String nameText, String emailText, String phoneNumberText) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nameText)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Name's update succeeded.",
                                        Toast.LENGTH_SHORT).show();
                                databaseName = nameText;
                            }else{
                                Toast.makeText(getActivity(), "Name's update failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
        try{
            user.updateEmail(emailText);
            Toast.makeText(getActivity(), "Email's update succeeded.",
                    Toast.LENGTH_SHORT).show();
                    databaseEmail = emailText;
        }
        catch(Exception e){
            Toast.makeText(getActivity(), "Email's update failed.",
                    Toast.LENGTH_SHORT).show();
        }


    }

    private final TextWatcher SaveProfileWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String nameText = editName.getText().toString().trim();
            String emailText = editEmail.getText().toString().trim();


            Boolean allTheSame = nameText.equals(databaseName)&& emailText.equals(databaseEmail);


            if(allTheSame || !EmailValidator.checkPattern(emailText)){
                saveButton.setEnabled(false);
            }else{
                saveButton.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}