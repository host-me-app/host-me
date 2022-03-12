package ch.epfl.sweng.hostme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class UserCreation extends AppCompatActivity {

    public final static String GENDER = "Gender";
    public final static String MALE = "Male";
    public final static String FEMALE = "Female";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_gender);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        RadioGroup radioGroup = findViewById(R.id.radioGrp);
        Button genderButt = findViewById(R.id.genderNextButton);
        genderButt.setOnClickListener(view -> {
            int selectedGender = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedButton = findViewById(selectedGender);
            String gender = selectedButton.getText().toString().equals(MALE) ? MALE : FEMALE;
            UserCreationPage5.DATA.put(GENDER, gender);
            goToFragment2();
        });

    }

    private void goToFragment2() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new FragmentCreationPage2());
        fragmentTransaction.commit();
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

}
