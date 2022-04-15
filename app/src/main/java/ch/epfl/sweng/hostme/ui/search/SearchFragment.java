package ch.epfl.sweng.hostme.ui.search;

import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.utils.Apartment;

public class SearchFragment extends Fragment {

    public static final float MAX_AREA = 3000f;
    public static final float MAX_PRICE = 5000f;
    private final CollectionReference reference = Database.getCollection(APARTMENTS);
    private final CollectionReference favReference = Database.getCollection("favorite_apart");
    private static final String FAVORITES = "favorites";
    private ApartmentAdapter recyclerAdapter;
    private Button filterButt;
    private boolean filterIsClicked;
    private RangeSlider rangeBarPrice;
    private RangeSlider rangeBarArea;
    private LinearLayout filters;
    private ArrayList<Apartment> apartments;
    private String searchText;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private View root;
    private Button clearFilters;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.recycler_view, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        rangeBarPrice = root.findViewById(R.id.range_bar_price);
        rangeBarArea = root.findViewById(R.id.range_bar_area);
        filterButt = root.findViewById(R.id.filters);
        filters = root.findViewById(R.id.all_filters);
        SearchView searchView = root.findViewById(R.id.search_view);
        clearFilters = root.findViewById(R.id.clear_filters);
        clearFilters.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchText = s.toLowerCase();
                updateRecyclerView(rangeBarPrice.getValues().get(0), rangeBarPrice.getValues().get(1),
                        rangeBarArea.getValues().get(0), rangeBarArea.getValues().get(1));
                return true;
            }
        });

        filterIsClicked = false;
        filterButt.setOnClickListener(view -> changeFiltersViewAndUpdate());
        clearFilters.setOnClickListener(view -> setRangeBar());

        changeFilterVisibility(View.GONE);
        setRangeBar();
        apartments = new ArrayList<>();
        setUpRecyclerView();

        return root;
    }

    /**
     * set the range bar to default values
     */
    private void setRangeBar() {
        rangeBarPrice.setValues(0f, MAX_PRICE);
        rangeBarPrice.setLabelFormatter(value -> value + " CHF");
        rangeBarArea.setValues(0f, MAX_AREA);
        rangeBarArea.setLabelFormatter(value -> value + " m²");
    }

    /**
     * Close or open the filters and update if necessary
     */
    private void changeFiltersViewAndUpdate() {
        if (!filterIsClicked) {
            clearFilters.setVisibility(View.VISIBLE);
            changeFilterVisibility(View.VISIBLE);
            filterButt.setText(R.string.apply_filters);
            filterIsClicked = true;
        } else {
            clearFilters.setVisibility(View.GONE);
            changeFilterVisibility(View.GONE);
            filterButt.setText(R.string.filters_text);
            filterIsClicked = false;
            updateRecyclerView(rangeBarPrice.getValues().get(0), rangeBarPrice.getValues().get(1),
                    rangeBarArea.getValues().get(0), rangeBarArea.getValues().get(1));
        }
    }


    /**
     * display or not all the filters
     *
     * @param visible GONE or INVISIBLE
     */
    private void changeFilterVisibility(int visible) {
        filters.setVisibility(visible);
    }

    /**
     * set up or update the recycler view
     */
    private void updateRecyclerView(Float min, Float max, Float min2, Float max2) {
        apartments = new ArrayList<>();
        reference.get().addOnCompleteListener(task -> {
            apartments.clear();
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                for (DocumentSnapshot doc : snapshot.getDocuments()) {
                    Apartment apartment = doc.toObject(Apartment.class);
                    Long rent = (Long) doc.get("rent");
                    Long area = (Long) doc.get("area");
                    String city = (String) doc.get("city");
                    String address = (String) doc.get("address");
                    Long npa = (Long) doc.get("npa");
                    apartment.setDocID(doc.getId());
                    if ((min <= rent) && (rent <= max) && (min2 <= area) && (area <= max2) && (searchText == null)) {
                        if (apartments.size() < 10)
                            apartments.add(apartment);
                    } else if ((min <= rent) && (rent <= max) && (min2 <= area) && (area <= max2)) {
                        if (String.valueOf(npa).toLowerCase().contains(searchText) ||
                                address.toLowerCase().contains(searchText) || city.toLowerCase().contains(searchText)) {
                            if (apartments.size() < 10)
                                apartments.add(apartment);
                        }
                    }
                }
                List<Apartment> apartmentsWithoutDuplicate = new ArrayList<>(new HashSet<>(apartments));
                recyclerAdapter.setApartments(apartmentsWithoutDuplicate);
                recyclerAdapter.notifyDataSetChanged();
            }
        });

    }


    /**
     * Initialize the recycler view with no filtered apartments
     */
    private void setUpRecyclerView() {
        apartments = new ArrayList<>();
        reference.get().addOnCompleteListener(task -> {
            apartments.clear();
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                for (DocumentSnapshot doc : snapshot.getDocuments()) {
                    if (apartments.size() < 10) {
                        Apartment apartment = doc.toObject(Apartment.class);
                        apartment.setDocID(doc.getId());
                        checkIfApartIsFavorite(apartment);
                        apartments.add(apartment);
                    }
                }
                List<Apartment> apartmentsWithoutDuplicate = new ArrayList<>(new HashSet<>(apartments));
                recyclerAdapter = new ApartmentAdapter(apartmentsWithoutDuplicate, root.getContext());
                recyclerView.setHasFixedSize(true);
                linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                recyclerView.setAdapter(recyclerAdapter);
            }
        });
    }

    /**
     * Check if the apart is a favorite one to update the buttons
     *
     * @param apartment
     */
    private void checkIfApartIsFavorite(Apartment apartment) {
        apartment.setFavorite(false);
        favReference.document(Auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        List<String> apartIDs = (List<String>) doc.get(FAVORITES);
                        assert apartIDs != null;
                        if (apartIDs.contains(apartment.getDocID())) {
                            apartment.setFavorite(true);
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
