package ch.epfl.sweng.hostme.activities;

import static ch.epfl.sweng.hostme.utils.Constants.ADDRESS;
import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;
import static ch.epfl.sweng.hostme.utils.Constants.APART_ID;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.CITY;
import static ch.epfl.sweng.hostme.utils.Constants.FROM;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_USER;
import static ch.epfl.sweng.hostme.utils.Constants.LEASE;
import static ch.epfl.sweng.hostme.utils.Constants.NPA;
import static ch.epfl.sweng.hostme.utils.Constants.PROPRIETOR;
import static ch.epfl.sweng.hostme.utils.Constants.RENT;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.DocumentReference;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.fragments.GradeApartment;
import ch.epfl.sweng.hostme.users.User;

public class InfoActivity extends AppCompatActivity {

    private TextView chatName;
    private String apartId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        this.chatName = findViewById(R.id.chat_name);
        this.loadReceiverDetails();
        if (this.apartId != null && !this.apartId.isEmpty()) {
            this.displayInfo();
            Button gradeButton = findViewById(R.id.grade_button_info);
            gradeButton.setOnClickListener(v -> this.goToGradeFragment());
        }
    }

    private void loadReceiverDetails() {
        User receiverUser = (User) getIntent().getSerializableExtra(KEY_USER);
        this.apartId = getIntent().getStringExtra(FROM);
        this.chatName.setText(receiverUser.getName());
    }

    private void displayInfo() {
        DocumentReference docRef = Database.getCollection(APARTMENTS).document(this.apartId);
        docRef.get()
                .addOnSuccessListener(result -> {
                    changeText(result.get(NPA), R.id.npa);
                    changeText(result.get(CITY), R.id.city);
                    changeText(result.get(ADDRESS), R.id.address);
                    changeText(result.get(AREA), R.id.area);
                    changeText(result.get(RENT), R.id.price);
                    changeText(result.get(LEASE), R.id.lease);
                    changeText(result.get(PROPRIETOR), R.id.proprietor);
                });
    }

    private void changeText(Object value, int id) {
        TextView key = findViewById(id);
        if (value != null) {
            key.setText(value.toString());
        } else {
            key.setText("");
        }
    }

    private void goToGradeFragment() {
        Bundle bundle = new Bundle();
        Fragment fragment = new GradeApartment();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        bundle.putString(APART_ID, this.apartId);
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.infoContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}