package ch.epfl.sweng.hostme.userCreation;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.utils.EmailValidator;


public class FragmentCreationPage5 extends Fragment {

    public static final String MAIL = "Mail";
    private EditText mail;
    private Button nextMailButt;
    private TextWatcher mailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String mailText = mail.getText().toString().trim();
            if (!EmailValidator.isValid(mailText)) {
                mail.setError("You should enter a valid mail address");
            } else {
                mail.setError(null);
            }
            nextMailButt.setEnabled(EmailValidator.isValid(mailText));
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_page5, container, false);
        mail = view.findViewById(R.id.mail);
        mail.addTextChangedListener(mailTextWatcher);

        nextMailButt = view.findViewById(R.id.nextButtonMail);
        nextMailButt.setEnabled(false);
        nextMailButt.setOnClickListener(v -> {
            String mailText = mail.getText().toString();
            FragmentCreationPage6.DATA.put(MAIL, mailText);
            goToFragment6();
        });

        return view;
    }

    /**
     * Go to password fragment
     */
    private void goToFragment6() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new FragmentCreationPage6());
        fragmentTransaction.commit();
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

}