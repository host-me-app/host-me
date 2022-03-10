package ch.epfl.sweng.hostme;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class EnterMailChangePwd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pwd);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
    }

}
