package ch.epfl.sweng.hostme.fragments;

import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_USERS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.account.Profile;
import ch.epfl.sweng.hostme.activities.MenuActivity;
import ch.epfl.sweng.hostme.creation.PasswordValidator;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.utils.IOnBackPressed;


public class CreationPasswordFragment extends Fragment implements IOnBackPressed {

    public final static Map<String, String> DATA = new HashMap<>();
    private static final String PREF_USER_NAME = "username";
    private static final String INVALID_PWD = "The password must be at least 8 characters and contains at " +
            "least 1 uppercase character and 1 special character";
    private static final String INVALID_CONFIRM_PWD = "The passwords should be identical";
    private static final String AUTH_FAILED = "Authentication failed";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_page6, container, false);

        EditText pwd = view.findViewById(R.id.password);
        EditText confirm_pwd = view.findViewById(R.id.confirm_pwd);
        Button terminateButt = view.findViewById(R.id.terminate_button);
        terminateButt.setOnClickListener(v -> {
            String pwdText = pwd.getText().toString();
            String confirm_pwdText = confirm_pwd.getText().toString();
            if (PasswordValidator.isValid(pwdText)) {
                if (pwdText.equals(confirm_pwdText)) {
                    createUser(DATA.get(CreationMailFragment.MAIL), pwdText);
                } else {
                    confirm_pwd.setError(INVALID_CONFIRM_PWD);
                }
            } else {
                pwd.setError(INVALID_PWD);
            }
        });

        return view;
    }

    /**
     * Go to menu activity
     */
    private void goToMenu() {
        Intent intent = new Intent(getActivity(), MenuActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    /**
     * Create a user on firebase
     */
    private void createUser(String email, String password) {
        Auth.createUser(email, password)
                .addOnSuccessListener(authResult -> {
                    setSharedPref(email);
                    updateFireStoreDB();
                    goToMenu();
                })
                .addOnFailureListener(r -> Toast.makeText(getActivity(), AUTH_FAILED, Toast.LENGTH_SHORT).show());
    }

    /**
     * Change the sharedPreferences to keep the user logged in
     */
    private void setSharedPref(String email) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editor.putString(PREF_USER_NAME, email);
        editor.apply();
    }

    /**
     * Update the database with user's attributes
     */
    private void updateFireStoreDB() {
        Profile user = new Profile(
                DATA.get(CreationFirstNameFragment.FIRST_NAME),
                DATA.get(CreationLastNameFragment.LAST_NAME),
                DATA.get(CreationMailFragment.MAIL),
                DATA.get(CreationGenderFragment.GENDER),
                DATA.get(CreationSchoolFragment.SCHOOL)
        );

        Database.getCollection(KEY_COLLECTION_USERS).document(Auth.getUid()).set(user);
    }

    @Override
    public boolean onBackPressed() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new CreationMailFragment());
        fragmentTransaction.commit();
        requireActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
        return true;
    }

}