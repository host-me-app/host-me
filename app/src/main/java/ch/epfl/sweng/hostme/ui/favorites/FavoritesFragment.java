package ch.epfl.sweng.hostme.ui.favorites;

import static ch.epfl.sweng.hostme.utils.Constants.BITMAP_FAV;

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
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.ui.search.ApartmentAdapter;
import ch.epfl.sweng.hostme.utils.Apartment;

public class FavoritesFragment extends Fragment {

    private static final String NBR = "nbr";
    private static final String FAVORITES = "favorites";
    private final CollectionReference reference = Database.getCollection("favorite_apart");
    private final CollectionReference apartReference = Database.getCollection("apartments");
    private View root;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ApartmentAdapter recyclerAdapter;
    private TextView noFavMessage;
    List<Apartment> apartments = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private int nbrApart;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = root.findViewById(R.id.favorites_recyclerView);
        noFavMessage = root.findViewById(R.id.no_fav_message);

        sharedPreferences = getContext().getSharedPreferences(BITMAP_FAV, Context.MODE_PRIVATE);
        reference.document(Auth.getUid()).addSnapshotListener((value, error) -> {
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
        try {
            reference.document(uid)
                    .get()
                    .addOnSuccessListener(result -> {
                        apartments.clear();
                        DocumentSnapshot doc = result;
                        List<String> apartIDs = (List<String>) doc.get(FAVORITES);
                        if (apartIDs.isEmpty()) {
                            apartments.clear();
                            noFavMessage.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            noFavMessage.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for (String apartID : apartIDs) {
                                getCorrespondingApartAndDisplay(apartID, apartments);
                            }
                        }
                    });
        } catch (Exception ignored) {
        }
    }

    /**
     * get the apart corresponding to the ID in Firestore and display it
     *
     * @param apartID
     * @param apartments
     */
    private void getCorrespondingApartAndDisplay(String apartID, List<Apartment> apartments) {
        apartReference.document(apartID)
                .get()
                .addOnSuccessListener(result -> {
                    Apartment apartment = result.toObject(Apartment.class);
                    apartment.setDocID(result.getId());
                    apartments.add(apartment);
                    displayRecycler(apartments);
                });
    }

    /**
     * prepare the layout and set the adapter to display the recyclerView
     *
     * @param apartments
     */
    private void displayRecycler(List<Apartment> apartments) {
        /*if (sharedPreferences.getInt(NBR, 0) != 0) {
            for (int i = 1; i <= sharedPreferences.getInt(NBR, 0); i++) {
                Gson gson = new Gson();
                String json = sharedPreferences.getString(FAVORITES + i, "");
                Apartment apart = gson.fromJson(json, Apartment.class);
                apartments.add(apart);
            }
            setUpRecyclerView(apartments);
        }*/
        List<Apartment> apartmentsWithoutDuplicate = new ArrayList<>(new HashSet<>(apartments));
        recyclerAdapter = new ApartmentAdapter(apartmentsWithoutDuplicate, root.getContext());
        recyclerAdapter.setFavFragment();
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}