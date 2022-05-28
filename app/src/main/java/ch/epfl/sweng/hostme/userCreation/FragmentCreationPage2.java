package ch.epfl.sweng.hostme.userCreation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.ui.IOnBackPressed;

public class FragmentCreationPage2 extends Fragment implements IOnBackPressed {

    public final static String FIRST_NAME = "firstName";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_page2, container, false);
        EditText userName = view.findViewById(R.id.first_name);
        Button nameButt = view.findViewById(R.id.next_button_first_name);
        nameButt.setOnClickListener(v -> {
            String name = userName.getText().toString();
            FragmentCreationPage6.DATA.put(FIRST_NAME, name);
            goToFragment3();
        });
        return view;
    }

    /**
     * Go to last name fragment
     */
    private void goToFragment3() {
        changeFragment(new FragmentCreationPage3());
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
        changeFragment(new FragmentCreationPage1());
        return true;
    }
}