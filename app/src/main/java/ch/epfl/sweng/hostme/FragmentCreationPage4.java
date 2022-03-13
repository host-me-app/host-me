package ch.epfl.sweng.hostme;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class FragmentCreationPage4 extends Fragment {

    public static final String MAIL = "Mail";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_page4, container, false);
        EditText mail = view.findViewById(R.id.mail);

        Button nextMailButt = view.findViewById(R.id.nextButtonMail);
        nextMailButt.setOnClickListener(v -> {
            String mailText = mail.getText().toString();
            if (EmailValidator.checkPattern(mailText)) {
                FragmentCreationPage5.DATA.put(MAIL, mailText);
                goToFragment5();
            }
        });

        return view;
    }

    private void goToFragment5() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new FragmentCreationPage5());
        fragmentTransaction.commit();
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

}