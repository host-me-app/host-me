package ch.epfl.sweng.hostme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentCreationPage2 extends Fragment {
    public final static String FIRST_NAME = "firstName";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_page2, container, false);
        EditText userName = view.findViewById(R.id.firstName);
        Button nameButt = view.findViewById(R.id.nextButtonFirstName);
        nameButt.setOnClickListener(v -> {
            String name = userName.getText().toString();
            FragmentCreationPage5.DATA.put(FIRST_NAME, name);
            goToFragment3();
        });
        return view;
    }

    private void goToFragment3() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new FragmentCreationPage3());
        fragmentTransaction.commit();
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }
}