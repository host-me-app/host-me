package ch.epfl.sweng.hostme;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;


public class FragmentCreationPage5 extends Fragment {
    public final static Map<String, String> DATA = new HashMap<>();
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_page5, container, false);
        mAuth = FirebaseAuth.getInstance();


        Button terminateButt = view.findViewById(R.id.terminateButton);
        EditText pwd = view.findViewById(R.id.password);
        EditText confirm_pwd = view.findViewById(R.id.confirm_pwd);

        terminateButt.setOnClickListener(v -> {
            String pwdText = pwd.getText().toString();
            String confirm_pwdText = confirm_pwd.getText().toString();
            if (pwdText.equals(confirm_pwdText) && PasswordValidator.isValid(pwdText)) {
                createUser(DATA.get(UserCreationPage4.MAIL), pwdText);
            }
        });

        return view;
    }

    private void welcome() {
        Intent intent = new Intent(getActivity(), WelcomePage.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getActivity(), "Authentication successed.",
                                Toast.LENGTH_SHORT).show();
                        welcome();
                    } else {
                        Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}