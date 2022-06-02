package ch.epfl.sweng.hostme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ch.epfl.sweng.hostme.R;


public class CreationPage1Fragment extends Fragment {

    public final static String GENDER = "Gender";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_page1, container, false);

        RadioGroup radioGroup = view.findViewById(R.id.radio_grp);
        Button genderButt = view.findViewById(R.id.genderNextButton);
        genderButt.setOnClickListener(v -> {
            int selectedGender = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedButton = view.findViewById(selectedGender);
            String gender = selectedButton.getText().toString();
            CreationPage6Fragment.DATA.put(GENDER, gender);
            goToFragment2();
        });

        return view;
    }

    /**
     * Go to first name fragment
     */
    private void goToFragment2() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new CreationPage2Fragment());
        fragmentTransaction.commit();
        requireActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

}