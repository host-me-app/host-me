package ch.epfl.sweng.hostme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.utils.IOnBackPressed;


public class CreationPage3Fragment extends Fragment implements IOnBackPressed {

    public final static String LAST_NAME = "lastName";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_page3, container, false);

        EditText lastName = view.findViewById(R.id.last_name);
        Button nameButt = view.findViewById(R.id.next_button_last_name);
        nameButt.setOnClickListener(v -> {
            String userLastName = lastName.getText().toString();
            CreationPage6Fragment.DATA.put(LAST_NAME, userLastName);
            goToFragment4();
        });

        return view;
    }

    /**
     * Go to email fragment
     */
    private void goToFragment4() {
        changeFragment(new CreationPage4Fragment());
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
        changeFragment(new CreationPage2Fragment());
        return true;
    }
}