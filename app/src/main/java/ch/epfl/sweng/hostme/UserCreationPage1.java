package ch.epfl.sweng.hostme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class UserCreationPage1 extends AppCompatActivity {

    public final static String GENDER = "Gender";
    public final static String MALE = "Male";
    public final static String FEMALE = "Female";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_gender);
        Objects.requireNonNull(this.getSupportActionBar()).hide();


        Button genderButt = findViewById(R.id.genderNextButton);
        RadioGroup radioGroup = findViewById(R.id.radioGrp);
        genderButt.setOnClickListener(view -> {
            int selectedGender = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedButton = findViewById(selectedGender);
            String gender = selectedButton.getText().toString() == MALE ? MALE : FEMALE;
            UserCreationPage5.DATA.put(GENDER, gender);
            goToPage2();
        });

    }

    private void goToPage2() {
        Intent intent = new Intent(UserCreationPage1.this, UserCreationPage2.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

}
