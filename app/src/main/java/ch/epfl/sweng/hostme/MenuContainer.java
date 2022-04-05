package ch.epfl.sweng.hostme;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class MenuContainer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new MenuFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
