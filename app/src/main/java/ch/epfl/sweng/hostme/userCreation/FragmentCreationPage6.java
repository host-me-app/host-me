package ch.epfl.sweng.hostme.userCreation;

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

import ch.epfl.sweng.hostme.MenuActivity;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.ui.IOnBackPressed;
import ch.epfl.sweng.hostme.utils.PasswordValidator;
import ch.epfl.sweng.hostme.utils.Profile;


public class FragmentCreationPage6 extends Fragment implements IOnBackPressed {
    public final static Map<String, String> DATA = new HashMap<>();
    private static final String PREF_USER_NAME = "username";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_page6, container, false);

        Button terminateButt = view.findViewById(R.id.terminateButton);
        EditText pwd = view.findViewById(R.id.password);
        EditText confirm_pwd = view.findViewById(R.id.confirm_pwd);

        terminateButt.setOnClickListener(v -> {
            String pwdText = pwd.getText().toString();
            String confirm_pwdText = confirm_pwd.getText().toString();
            if (pwdText.equals(confirm_pwdText) && PasswordValidator.isValid(pwdText)) {
                createUser(DATA.get(FragmentCreationPage5.MAIL), pwdText);
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
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    /**
     * Create a user on firebase
     *
     * @param email
     * @param password
     */
    private void createUser(String email, String password) {
        Auth.createUser(email, password)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                setSharedPref(email);
                                updateFireStoreDB();
                                Toast.makeText(getActivity(), "Authentication successed.",
                                        Toast.LENGTH_SHORT).show();
                                goToMenu();
                            } else {
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }

    /**
     * Change the sharedPreferences to keep the user logged in
     * @param email
     */
    private void setSharedPref(String email) {
        SharedPreferences.Editor editor = PreferenceManager.
                getDefaultSharedPreferences(getContext()).edit();
        editor.putString(PREF_USER_NAME, email);
        editor.apply();
    }

    /**
     * Update the database with user's attributes
     */
    private void updateFireStoreDB() {

        Profile user = new Profile(
                DATA.get(FragmentCreationPage2.FIRST_NAME),
                DATA.get(FragmentCreationPage3.LAST_NAME),
                DATA.get(FragmentCreationPage5.MAIL),
                DATA.get(FragmentCreationPage1.GENDER),
                DATA.get(FragmentCreationPage4.SCHOOL)
        );

        Database.getCollection("users").document(Auth.getUid()).set(user);

    }

    @Override
    public boolean onBackPressed() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new FragmentCreationPage5());
        fragmentTransaction.commit();
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
        return true;
    }

}