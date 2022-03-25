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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sweng.hostme.MainActivity;
import ch.epfl.sweng.hostme.WalletFragment;
import ch.epfl.sweng.hostme.utils.Profile;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.utils.EmailValidator;

public class AccountFragment extends Fragment {

    private View view;

    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private RadioGroup editGender;
    private RadioButton buttonM;
    private RadioButton buttonF;

    private Button saveButton;
    private Button logOutButton;
    private Button changePasswordButton;

    private String dbFirstName;
    private String dbLastName;
    private String dbEmail;
    private String dbGender;



    private final static FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        this.view = view;

        editFirstName = view.findViewById(R.id.userProfileFirstName);
        editLastName = view.findViewById(R.id.userProfileLastName);
        editEmail = view.findViewById(R.id.userProfileEmail);
        editGender = view.findViewById(R.id.userProfileRadioG);
        buttonM = view.findViewById(R.id.userProfileGenderM);
        buttonF = view.findViewById(R.id.userProfileGenderF);

        logOutButton = view.findViewById(R.id.userProfilelogOutButton);
        saveButton = view.findViewById(R.id.userProfileSaveButton);
        changePasswordButton = view.findViewById(R.id.userProfileChangePasswordButton);

        saveButton.setEnabled(false);


        mAuth = FirebaseAuth.getInstance();

        Button wallet_button = view.findViewById(R.id.wallet_button);
        wallet_button.setOnClickListener(v -> {
            goToWalletFragment();
        });

        DocumentReference docRef = database.collection("users")
                .document(mAuth.getUid());

        docRef.get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()){
                        Profile userInDB = task.getResult().toObject(Profile.class);
                        displayUIFromDB(userInDB);

                        editFirstName.addTextChangedListener(SaveProfileWatcher);
                        editLastName.addTextChangedListener(SaveProfileWatcher);
                        editEmail.addTextChangedListener(SaveProfileWatcher);
                        editGender.setOnCheckedChangeListener(SaveProfileCheckWatcher);

                        addListenerToSaveButton();
                        addListenerToChangePasswordButton();

                        logOutButton.setOnClickListener(v -> {
                            logUserOut();
                        });

                    }
                }
        );


        return view;
    }

    /**
     * Display to the UI the profile previously fetched from the database
     * @param userInDB Profile in Database
     */
    private void displayUIFromDB(Profile userInDB) {

        dbFirstName = userInDB.getFirstName();
        dbLastName = userInDB.getLastName();
        dbEmail = userInDB.getEmail();
        dbGender = userInDB.getGender();

        editFirstName.setText(dbFirstName);
        editLastName.setText(dbLastName);
        editEmail.setText(dbEmail);
        RadioButton selectButton = dbGender.equals("Male") ? buttonM : buttonF;
        selectButton.setChecked(true);
    }


    /**
     * Take data present in the UI and turn it into a Profile class
     * @return Profile
     */
    private Profile getProfileFromUI(){

        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String email= editEmail.getText().toString().trim();

        int selectedGender = editGender.getCheckedRadioButtonId();
        RadioButton selectedButton = view.findViewById(selectedGender);
        String gender = selectedButton.getText().toString().equals("Male") ? "Male" : "Female";

        return new Profile(firstName,lastName,email,gender);

    }


    /**
     * Add a listener to the button Save
     */
    private void addListenerToSaveButton(){

        saveButton.setOnClickListener(v -> {

            Profile toUpdateUser = getProfileFromUI();

            if (EmailValidator.checkPattern(toUpdateUser.getEmail())){
                saveUserProperties(toUpdateUser);
            }
            saveButton.setEnabled(false);

        });


    }

    /**
     * Add a listener to the change password button
     */
    private void addListenerToChangePasswordButton() {

        changePasswordButton.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);

        });
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
     * Save updated user's profile on the  database
     *
     * @param toUpdateUser the updated profile
     */
    private void saveUserProperties(Profile toUpdateUser) {

        database.collection("users").document(mAuth.getUid()).set(toUpdateUser)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                dbFirstName = toUpdateUser.getFirstName();
                                dbLastName = toUpdateUser.getLastName();
                                dbEmail = toUpdateUser.getEmail();
                                dbGender = toUpdateUser.getGender();

                                Toast.makeText(getActivity(), "Profile's update succeeded.",
                                        Toast.LENGTH_SHORT).show();

                                mAuth.getCurrentUser().updateEmail(toUpdateUser.getEmail()).addOnCompleteListener(
                                        task2 -> {
                                            if (task2.isSuccessful()) {
                                                dbEmail = toUpdateUser.getEmail();
                                                Toast.makeText(getActivity(), "Email's update succeeded.",
                                                        Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getActivity(), "Email's update failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );
                            }
                            else{
                                Toast.makeText(getActivity(), "Profile's update failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                );

    }


    /**
     * Watcher for any modifications of the gender button that is checked
     */
    private RadioGroup.OnCheckedChangeListener SaveProfileCheckWatcher  = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();
            String email= editEmail.getText().toString().trim();

            RadioButton selectedButton = view.findViewById(checkedId);
            String gender = selectedButton.getText().toString().equals("Male") ? "Male" : "Female";

            Boolean allTheSame = firstName.equals(dbFirstName)
                    &&lastName.equals(dbLastName)
                    &&firstName.equals(dbFirstName)
                    &&email.equals(dbEmail)
                    &&gender.equals(dbGender);


            if(allTheSame || !EmailValidator.checkPattern(email)){
                saveButton.setEnabled(false);
            }else{
                saveButton.setEnabled(true);
            }

        }

    };

    /**
     * Watcher for any modifications of the text in the fields of the profile
     */
    private final TextWatcher SaveProfileWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();
            String email= editEmail.getText().toString().trim();
            int selectedGender = editGender.getCheckedRadioButtonId();
            RadioButton selectedButton = view.findViewById(selectedGender);
            String gender = selectedButton.getText().toString().equals("Male") ? "Male" : "Female";

            Boolean allTheSame = firstName.equals(dbFirstName)
                    &&lastName.equals(dbLastName)
                    &&firstName.equals(dbFirstName)
                    &&email.equals(dbEmail)
                    &&gender.equals(dbGender);


            if(allTheSame || !EmailValidator.checkPattern(email)){
                saveButton.setEnabled(false);
            }else{
                saveButton.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    /**
     * Go to wallet fragment
     */
    private void goToWalletFragment() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_menu1, new WalletFragment());
        fragmentTransaction.commit();
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

}