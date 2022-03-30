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
    private SearchViewModel viewModel;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private ApartmentAdapter recyclerAdapter;
    private CollectionReference reference = firebaseFirestore.collection(APARTMENTS);
    private Button filterButt;
    private boolean filterIsClicked;
    private RangeSlider rangeBarPrice;
    private RangeSlider rangeBarArea;
    private View root;
    private LinearLayout filters;
    private SearchView searchView;
    private List<Apartment> apartments;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);

        rangeBarPrice = root.findViewById(R.id.range_bar_price);
        rangeBarArea = root.findViewById(R.id.range_bar_area);
        filterButt = root.findViewById(R.id.filters);
        filters = root.findViewById(R.id.all_filters);
        searchView = root.findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                apartments = new ArrayList<>();
                reference.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        apartments.clear();
                        QuerySnapshot snapshot = task.getResult();
                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            Apartment apartment = doc.toObject(Apartment.class);
                            String city = (String) doc.get("city");
                            String address = (String) doc.get("address");
                            Long npa = (Long) doc.get("npa");
                            String string = s.toLowerCase();
                            if (city.toLowerCase().contains(string) ||
                                    address.toLowerCase().contains(string) ||
                                    String.valueOf(npa).toLowerCase().contains(string)) {
                                apartments.add(apartment);
                            }
                            recyclerAdapter.setApartments(apartments);
                            recyclerAdapter.notifyDataSetChanged();
                        }
                    }
                });
                return true;
            }
        });


        filterIsClicked = false;
        filterButt.setOnClickListener(view -> {
            if (!filterIsClicked) {
                changeFilterVisibility(View.VISIBLE);
                filterButt.setText("Apply Filters");
                filterIsClicked = true;
            } else {
                changeFilterVisibility(View.GONE);
                filterButt.setText("Filters");
                filterIsClicked = false;
                updateRecyclerView("rent", rangeBarPrice.getValues().get(0), rangeBarPrice.getValues().get(1),
                       "area", rangeBarArea.getValues().get(0), rangeBarArea.getValues().get(1));
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
     * @param visible
     */
    private void changeFilterVisibility(int visible) {
        filters.setVisibility(visible);
    }

    /**
     * set up or update the recycler view
     */
    private void updateRecyclerView(String field, Float min, Float max, String field2, Float min2, Float max2) {
        // ------
        apartments = new ArrayList<>();
        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                for (DocumentSnapshot doc : snapshot.getDocuments()) {
                    Apartment apartment = doc.toObject(Apartment.class);
                    long rent = (long) doc.get(field);
                    long area = (long) doc.get(field2);
                    if (min <= rent && rent <= max && min2 <= area && area <= max2 &&
                            !apartments.contains(apartment)) {
                        apartments.add(apartment);
                    }
                    recyclerAdapter.setApartments(apartments);
                    recyclerAdapter.notifyDataSetChanged();
                }
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
