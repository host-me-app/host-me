package ch.epfl.sweng.hostme.ui.favorites;

import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.ui.search.ApartmentAdapter;
import ch.epfl.sweng.hostme.utils.Apartment;

public class FavoritesFragment extends Fragment {

    private View root;
    private ArrayList<Apartment> apartments;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private final CollectionReference reference = Database.getCollection("favorite_apart");
    private ApartmentAdapter recyclerAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = root.findViewById(R.id.favorites_recyclerView);
        apartments = new ArrayList<>();
        setUpRecyclerView();

        return root;
    }

    /**
     * Set up the page with all the favorite apartments of the user
     */
    private void setUpRecyclerView() {
        apartments = new ArrayList<>();
        String uid = Auth.getUid();
        reference.document(uid).get();
            /*apartments.clear();
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                for (DocumentReference doc : snapshot.getReference()) {
                    if (apartments.size() < 10) {
                        Apartment apartment = doc.toObject(Apartment.class);
                        apartment.setDocID(doc.getId());
                        apartments.add(apartment);
                    }
                }
                List<Apartment> apartmentsWithoutDuplicate = new ArrayList<>(new HashSet<>(apartments));
                recyclerAdapter = new ApartmentAdapter(apartmentsWithoutDuplicate);
                recyclerView.setHasFixedSize(true);
                linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                recyclerView.setAdapter(recyclerAdapter);
            }
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}