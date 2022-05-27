package ch.epfl.sweng.hostme.userCreation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.ui.IOnBackPressed;


public class FragmentCreationPage4 extends Fragment implements IOnBackPressed {

    public static final String SCHOOL = "School";
    String[] schools = {"EPFL", "EHL", "CHUV", "UNIL", "NONE"};
    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_page4, container, false);

        spinner = view.findViewById(R.id.school);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                schools);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        Button nextButtonSchool = view.findViewById(R.id.next_button_school);
        nextButtonSchool.setOnClickListener(v -> {
            FragmentCreationPage6.DATA.put(SCHOOL, spinner.getSelectedItem().toString());
            goToFragment5();
        });
        return view;
    }

    /**
     * Go to password fragment
     */
    private void goToFragment5() {
        changeFragment(new FragmentCreationPage5());
    }

    /**
     * Change the fragment (next or previous)
     */
    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        requireActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    @Override
    public boolean onBackPressed() {
        changeFragment(new FragmentCreationPage3());
        return true;
    }

}