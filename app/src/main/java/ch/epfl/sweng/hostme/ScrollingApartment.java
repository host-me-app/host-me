package ch.epfl.sweng.hostme;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ScrollingApartment extends AppCompatActivity {

    private final static FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private RecyclerviewApartments recyclerviewApartments;

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

        recyclerviewApartments = new RecyclerviewApartments(apartments);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerviewApartments);
        recyclerviewApartments.notifyDataSetChanged();


    }
}
