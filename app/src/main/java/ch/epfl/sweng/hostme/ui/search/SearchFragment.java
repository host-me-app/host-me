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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.hostme.Apartment;
import ch.epfl.sweng.hostme.LinearLayoutManagerWrapper;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.adapter.ApartmentAdapter;

public class SearchFragment extends Fragment {

    public static final float MAX_AREA = 3000f;
    public static final float MAX_PRICE = 5000f;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference reference = firebaseFirestore.collection(APARTMENTS);
    private SearchViewModel viewModel;
    private RecyclerView recyclerView;
    private ApartmentAdapter recyclerAdapter;
    private Button filterButt;
    private boolean filterIsClicked;
    private RangeSlider rangeBarPrice;
    private RangeSlider rangeBarArea;
    private LinearLayout filters;
    private List<Apartment> apartments;
    private String searchText;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);

        rangeBarPrice = root.findViewById(R.id.range_bar_price);
        rangeBarArea = root.findViewById(R.id.range_bar_area);
        filterButt = root.findViewById(R.id.filters);
        filters = root.findViewById(R.id.all_filters);
        SearchView searchView = root.findViewById(R.id.search_view);

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
        filterButt.setOnClickListener(view -> {
            if (!filterIsClicked) {
                changeFilterVisibility(View.VISIBLE);
                filterButt.setText(R.string.apply_filters);
                filterIsClicked = true;
            } else {
                changeFilterVisibility(View.GONE);
                filterButt.setText(R.string.filters_text);
                filterIsClicked = false;
                updateRecyclerView(rangeBarPrice.getValues().get(0), rangeBarPrice.getValues().get(1),
                        rangeBarArea.getValues().get(0), rangeBarArea.getValues().get(1));
            }
        });

        changeFilterVisibility(View.GONE);

        rangeBarPrice.setValues(0f, MAX_PRICE);
        rangeBarPrice.setLabelFormatter(value -> value + " CHF");
        rangeBarArea.setValues(0f, MAX_AREA);
        rangeBarArea.setLabelFormatter(value -> value + " m²");

        setUpRecyclerView();

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.getApartments().observe(getViewLifecycleOwner(), apartments ->
                recyclerAdapter.setApartments(apartments));
        return root;
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
        // ------
        apartments = new ArrayList<>();
        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                for (DocumentSnapshot doc : snapshot.getDocuments()) {
                    Apartment apartment = doc.toObject(Apartment.class);
                    Long rent = (Long) doc.get("rent");
                    Long area = (Long) doc.get("area");
                    String city = (String) doc.get("city");
                    String address = (String) doc.get("address");
                    Long npa = (Long) doc.get("npa");
                    if ((min <= rent) && (rent <= max) && (min2 <= area) && (area <= max2) && (searchText == null)) {
                        apartments.add(apartment);
                    } else if ((min <= rent) && (rent <= max) && (min2 <= area) && (area <= max2)) {
                        if (String.valueOf(npa).toLowerCase().contains(searchText) ||
                                address.toLowerCase().contains(searchText) || city.toLowerCase().contains(searchText)) {
                            apartments.add(apartment);
                        }
                    }
                }
                recyclerAdapter.setApartments(apartments);
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
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                for (DocumentSnapshot doc : snapshot.getDocuments()) {
                    apartments.add(doc.toObject(Apartment.class));
                }
                recyclerAdapter = new ApartmentAdapter(apartments);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(getContext()));
                recyclerView.setAdapter(recyclerAdapter);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
