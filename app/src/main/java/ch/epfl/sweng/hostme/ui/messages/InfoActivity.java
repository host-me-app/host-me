package ch.epfl.sweng.hostme.ui.messages;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import ch.epfl.sweng.hostme.databinding.ActivityInfoBinding;

public class InfoActivity extends AppCompatActivity {

    private ActivityInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(binding.getRoot());
    }
}
