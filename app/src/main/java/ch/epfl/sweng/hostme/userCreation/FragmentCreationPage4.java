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

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import ch.epfl.sweng.hostme.R;


public class FragmentCreationPage4 extends Fragment {

    public static final String SCHOOL = "School";
    String[] schools = {"EPFL", "EHL", "CHUV", "UNIL", "NONE"};
    private Spinner spinner;
    private Button nextButtonSchool;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_page4, container, false);
        nextButtonSchool = view.findViewById(R.id.nextButtonSchool);

        spinner = view.findViewById(R.id.school);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item,
                schools);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

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
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new FragmentCreationPage5());
        fragmentTransaction.commit();
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

}