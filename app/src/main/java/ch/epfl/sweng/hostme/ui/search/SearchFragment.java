package ch.epfl.sweng.hostme.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import ch.epfl.sweng.hostme.Apartment;
import ch.epfl.sweng.hostme.LinearLayoutManagerWrapper;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.adapter.ApartmentAdapter;

public class SearchFragment extends Fragment {

    private final static long NB_APARTMENT_TO_DISPLAY = 9;
    public static final String APARTMENTS = "apartments";
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private ApartmentAdapter recyclerAdapter;
    private CollectionReference reference = firebaseFirestore.collection(APARTMENTS);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);

        setUpRecyclerView();
        return root;
    }

    private void setUpRecyclerView() {
        Query query = reference.orderBy("rent", Query.Direction.ASCENDING).limit(NB_APARTMENT_TO_DISPLAY);
        FirestoreRecyclerOptions<Apartment> options = new FirestoreRecyclerOptions.Builder<Apartment>()
                .setQuery(query, Apartment.class)
                .setLifecycleOwner(this)
                .build();

        recyclerAdapter = new ApartmentAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(getContext()));
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }
}
