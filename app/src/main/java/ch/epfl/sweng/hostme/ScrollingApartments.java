package ch.epfl.sweng.hostme;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ScrollingApartments extends AppCompatActivity implements RecyclerViewApartments.MyViewHolder.ItemClickListener {

    private final static FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private RecyclerViewApartments recyclerviewApartments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrolling_menu);

        List<Apartment> apartments = new ArrayList<>();
        DB.collection("apartments").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        return;
                    } else {
                        apartments.addAll(queryDocumentSnapshots.toObjects(Apartment.class));
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),
                        "Error getting data!!!", Toast.LENGTH_LONG).show());


        recyclerView = findViewById(R.id.apartment_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerviewApartments = new RecyclerViewApartments(this, apartments);
        recyclerviewApartments.setClickListener(this);
        recyclerView.setAdapter(recyclerviewApartments);

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + recyclerviewApartments.getApart(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}