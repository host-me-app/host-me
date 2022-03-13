package ch.epfl.sweng.hostme;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class FragmentCreationPage3 extends Fragment {
    public final static String LAST_NAME = "lastName";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_page3, container, false);

        EditText lastName = view.findViewById(R.id.lastName);

        Button nameButt = view.findViewById(R.id.nextButtonLastName);
        nameButt.setOnClickListener(v -> {
            String userLastName = lastName.getText().toString();
            FragmentCreationPage5.DATA.put(LAST_NAME, userLastName);
            goToFragment4();
        });

        return view;
    }

    private void goToFragment4() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new FragmentCreationPage4());
        fragmentTransaction.commit();
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

}