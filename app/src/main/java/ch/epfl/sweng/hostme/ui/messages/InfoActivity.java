package ch.epfl.sweng.hostme.ui.messages;

import static ch.epfl.sweng.hostme.utils.Constants.APART_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.databinding.ActivityInfoBinding;
import ch.epfl.sweng.hostme.ui.search.GradeApartment;
import ch.epfl.sweng.hostme.users.User;
import ch.epfl.sweng.hostme.utils.Constants;
import ch.epfl.sweng.hostme.utils.Profile;

public class InfoActivity extends AppCompatActivity {

    private ActivityInfoBinding binding;
    private User receiverUser;
    private String apartId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadReceiverDetails();
        displayInfo();
        binding.gradeButton.setOnClickListener(this::goToGradeFragment);
    }

    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        apartId = (String) getIntent().getSerializableExtra(Constants.FROM);
        binding.chatName.setText(receiverUser.name);
    }

    private void displayInfo(){
        DocumentReference docRef = Database.getCollection(Constants.APARTMENTS).document(apartId);
        docRef.get().addOnSuccessListener(
                result -> {
                    changeText(result.get("npa"), R.id.npa);
                    changeText(result.get("city"), R.id.city);
                    changeText(result.get("address"), R.id.addr);
                    changeText(result.get("area"), R.id.area);
                    changeText(result.get("rent"), R.id.price);
                    changeText(result.get("currentLease"), R.id.lease);
                    changeText(result.get("proprietor"), R.id.proprietor);
                }
        );
    }

    private void changeText(Object addr, int id) {
        TextView addrText = binding.getRoot().findViewById(id);
        String address = "null";
        if(addr != null){
            addrText.setText(addr.toString());
        }else{
            addrText.setText(address);
        }
    }

    private void goToGradeFragment(View view) {
        Bundle bundle = new Bundle();
        Fragment fragment = new GradeApartment();
        FragmentTransaction fragmentTransaction =
                ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
        bundle.putString(APART_ID, this.apartId);
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.infoContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}