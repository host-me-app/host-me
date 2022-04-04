package ch.epfl.sweng.hostme.userCreation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import ch.epfl.sweng.hostme.R;

public class CreationContainer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new FragmentCreationPage1());
        fragmentTransaction.commit();
    }

}