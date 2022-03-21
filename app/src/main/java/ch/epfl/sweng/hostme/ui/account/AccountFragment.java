package ch.epfl.sweng.hostme.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sweng.hostme.MainActivity;
import ch.epfl.sweng.hostme.Profile;
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
    private EditText editPhoneNumber;

    private Button saveButton;
    private Button logOutButton;

    private String dbFirstName;
    private String dbLastName;
    private String dbEmail;
    private String dbGender;
    private String dbPhoneNumber;


    private final static FirebaseFirestore database = FirebaseFirestore.getInstance();
    //    private FragmentAccountBinding binding;
    private FirebaseUser userFire;
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
        editPhoneNumber = view.findViewById(R.id.userProfilePhone);
        editGender = view.findViewById(R.id.userProfileRadioG);
        buttonM = view.findViewById(R.id.userProfileGenderM);
        buttonF = view.findViewById(R.id.userProfileGenderF);

        logOutButton = view.findViewById(R.id.userProfilelogOutButton);
        saveButton = view.findViewById(R.id.userProfileSaveButton);

        saveButton.setEnabled(false);


        mAuth = FirebaseAuth.getInstance();

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
                        editPhoneNumber.addTextChangedListener(SaveProfileWatcher);
                        editGender.setOnCheckedChangeListener(SaveProfileCheckWatcher);

                        addListenerToSaveButton();

                        logOutButton.setOnClickListener(v -> {
                            logUserOut();
                        });

                    }
                }
        );


        Log.d( "TAG","sal ");

        return view;
    }

    private void displayUIFromDB(Profile userInDB) {

        dbFirstName = userInDB.getFirstName();
        dbLastName = userInDB.getLastName();
        dbEmail = userInDB.getEmail();
        dbGender = userInDB.getGender();
        dbPhoneNumber = userInDB.getPhoneNumber();

        editFirstName.setText(dbFirstName);
        editLastName.setText(dbLastName);
        editEmail.setText(dbEmail);
        RadioButton selectButton = dbGender.equals("Male") ? buttonM : buttonF;
        selectButton.setChecked(true);
        editPhoneNumber.setText(dbPhoneNumber);
    }

    private Profile getProfileFromUI(){

        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String email= editEmail.getText().toString().trim();
        String phoneNumber = editPhoneNumber.getText().toString().trim();

        int selectedGender = editGender.getCheckedRadioButtonId();
        RadioButton selectedButton = view.findViewById(selectedGender);
        String gender = selectedButton.getText().toString().equals("Male") ? "Male" : "Female";

        return new Profile(firstName,lastName,email,gender,phoneNumber);

    }


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
                                dbPhoneNumber = toUpdateUser.getPhoneNumber();

                                Toast.makeText(getActivity(), "Profile's update succeeded.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(), "Profile's update failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                );


//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(toUpdateUser.getFirstName())
//                .build();
//
//        userFire.updateProfile(profileUpdates)
//                .addOnCompleteListener(
//                        task -> {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(getActivity(), "Display name's update succeeded.",
//                                        Toast.LENGTH_SHORT).show();
//                                dbFirstName = toUpdateUser.getFirstName();
//                            }else{
//                                Toast.makeText(getActivity(), "Display name's update failed.",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        });

//        try{
//            userFire.updateEmail(toUpdateUser.getEmail());
//
//        }
//        catch(Exception e){
//            Toast.makeText(getActivity(), "Profile's update failed.",
//                    Toast.LENGTH_SHORT).show();
//        }


    }

    private RadioGroup.OnCheckedChangeListener SaveProfileCheckWatcher  = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();
            String email= editEmail.getText().toString().trim();
            String phoneNumber = editPhoneNumber.getText().toString().trim();

            RadioButton selectedButton = view.findViewById(checkedId);
            String gender = selectedButton.getText().toString().equals("Male") ? "Male" : "Female";

            Boolean allTheSame = firstName.equals(dbFirstName)
                    &&lastName.equals(dbLastName)
                    &&firstName.equals(dbFirstName)
                    &&email.equals(dbEmail)
                    &&phoneNumber.equals(dbPhoneNumber)
                    &&gender.equals(dbGender);


            if(allTheSame || !EmailValidator.checkPattern(email)){
                saveButton.setEnabled(false);
            }else{
                saveButton.setEnabled(true);
            }

        }

    };


    private final TextWatcher SaveProfileWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();
            String email= editEmail.getText().toString().trim();
            String phoneNumber = editPhoneNumber.getText().toString().trim();
            int selectedGender = editGender.getCheckedRadioButtonId();
            RadioButton selectedButton = view.findViewById(selectedGender);
            String gender = selectedButton.getText().toString().equals("Male") ? "Male" : "Female";

            Boolean allTheSame = firstName.equals(dbFirstName)
                    &&lastName.equals(dbLastName)
                    &&firstName.equals(dbFirstName)
                    &&email.equals(dbEmail)
                    &&phoneNumber.equals(dbPhoneNumber)
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

}