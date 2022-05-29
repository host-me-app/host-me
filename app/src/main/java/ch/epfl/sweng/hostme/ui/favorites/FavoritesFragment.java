package ch.epfl.sweng.hostme.ui.favorites;

import static ch.epfl.sweng.hostme.utils.Constants.BITMAP_FAV;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_APARTMENTS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_FAV;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.ui.search.ApartmentAdapter;
import ch.epfl.sweng.hostme.utils.Apartment;

public class FavoritesFragment extends Fragment {

    private static final String FAVORITES = "favorites";
    private final CollectionReference reference = Database.getCollection(KEY_COLLECTION_FAV);
    private final CollectionReference apartReference = Database.getCollection(KEY_COLLECTION_APARTMENTS);
    List<Apartment> apartments = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noFavMessage;
    private SharedPreferences bitmapPreferences;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        this.recyclerView = root.findViewById(R.id.favorites_recyclerView);
        this.noFavMessage = root.findViewById(R.id.no_fav_message);

        this.bitmapPreferences = requireContext().getSharedPreferences(BITMAP_FAV, Context.MODE_PRIVATE);

        this.reference.document(Auth.getUid()).addSnapshotListener((value, error) -> {
            if (value != null && value.exists()) {
                setUpRecyclerView(apartments);
            }
        });

        return root;
    }


    /**
     * Set up the page with all the favorite apartments of the user
     */
    private void setUpRecyclerView(List<Apartment> apartments) {
        String uid = Auth.getUid();
        this.reference.document(uid).get()
                .addOnSuccessListener(result -> {
                    apartments.clear();
                    List<String> apartIDs = (List<String>) result.get(FAVORITES);
                    if (Objects.requireNonNull(apartIDs).isEmpty()) {
                        this.bitmapPreferences.edit().clear().apply();
                        apartments.clear();
                        this.noFavMessage.setVisibility(View.VISIBLE);
                        this.recyclerView.setVisibility(View.GONE);
                    } else {
                        this.noFavMessage.setVisibility(View.GONE);
                        this.recyclerView.setVisibility(View.VISIBLE);
                        for (String apartID : apartIDs) {
                            getCorrespondingApartAndDisplay(apartID, apartments);
                        }
                    }
                });
    }

    /**
     * Get the apartment corresponding to the ID in Firestore and display it
     */
    private void getCorrespondingApartAndDisplay(String apartID, List<Apartment> apartments) {
        this.apartReference.document(apartID).get()
                .addOnSuccessListener(result -> {
                    Apartment apartment = result.toObject(Apartment.class);
                    apartments.add(apartment);
                    displayRecycler(apartments);
                });
    }

    /**
     * Prepare the layout and set the adapter to display the recyclerView
     */
    private void displayRecycler(List<Apartment> apartments) {
        List<Apartment> apartmentsWithoutDuplicate = new ArrayList<>(new HashSet<>(apartments));
        ApartmentAdapter recyclerAdapter = new ApartmentAdapter(apartmentsWithoutDuplicate);
        recyclerAdapter.setFavFragment();
        this.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setAdapter(recyclerAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}