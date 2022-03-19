package ch.epfl.sweng.hostme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.BreakIterator;

public class MainScrollingApart extends AppCompatActivity {

    private final static long NB_APARTMENT_TO_DISPLAY = 10;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        Query query = firebaseFirestore.collection("apartments").limit(NB_APARTMENT_TO_DISPLAY);
        FirestoreRecyclerOptions<Apartment> options = new FirestoreRecyclerOptions.Builder<Apartment>()
                .setQuery(query, Apartment.class)
                .build();

        recyclerAdapter = new FirestoreRecyclerAdapter<Apartment, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Apartment model) {
                holder.addr.setText(model.getAddress());
                holder.price.setText(String.valueOf(model.getRent()));
                holder.area.setText(model.getArea());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                return new ViewHolder(view);
            }

        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView price;
        public TextView addr;
        public TextView area;
        public ConstraintLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.price = itemView.findViewById(R.id.list_price);
            this.addr = itemView.findViewById(R.id.list_addr);
            this.area = itemView.findViewById(R.id.list_area);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }


}
