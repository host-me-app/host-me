package ch.epfl.sweng.hostme.ui.search;

import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import ch.epfl.sweng.hostme.Apartment;
import ch.epfl.sweng.hostme.LinearLayoutManagerWrapper;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.adapter.ApartmentAdapter;

public class SearchFragment extends Fragment {

    public static final float MAX_AREA = 3000f;
    public static final float MAX_PRICE = 5000f;
    private SearchViewModel viewModel;
    private final static long NB_APARTMENT_TO_DISPLAY = 9;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private ApartmentAdapter recyclerAdapter;
    private CollectionReference reference = firebaseFirestore.collection(APARTMENTS);
    private Button filterButt;
    private boolean filterIsClicked;
    private RangeSlider rangeBarPrice;
    private RangeSlider rangeBarArea;
    private View root;
    private LinearLayout filterArea;
    private LinearLayout filterPrice;
    private Query query;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);

        rangeBarPrice = root.findViewById(R.id.range_bar_price);
        rangeBarArea = root.findViewById(R.id.range_bar_area);
        filterButt = root.findViewById(R.id.filters);

        filterArea = root.findViewById(R.id.filter_area);
        filterPrice = root.findViewById(R.id.filter_price);

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
                updateRecyclerView("rent", rangeBarPrice.getValues().get(0), rangeBarPrice.getValues().get(1));
                //updateRecyclerView("area", rangeBarArea.getValues().get(0), rangeBarArea.getValues().get(1));
            }
        });

        changeFilterVisibility(View.GONE);

        rangeBarPrice.setValues(0f, MAX_PRICE);
        rangeBarPrice.setLabelFormatter(value -> value + " CHF");
        rangeBarArea.setValues(0f, MAX_AREA);
        rangeBarArea.setLabelFormatter(value -> value + " m²");


        updateRecyclerView(null, null, null);


        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.getApartments().observe(getViewLifecycleOwner(), apartments ->
                recyclerAdapter.setApartments(apartments));
        return root;
    }

    private void changeFilterVisibility(int visible) {
        filterArea.setVisibility(visible);
        filterPrice.setVisibility(visible);
    }

    /**
     * set up or update the recycler view
     */
    private void updateRecyclerView(String field, Float min, Float max) {
        // ------
        if (field == null) {
            query = reference.limit(NB_APARTMENT_TO_DISPLAY);
        } else {
            query = reference.whereGreaterThanOrEqualTo(field, min)
                    .whereLessThanOrEqualTo(field, max);
        }

        FirestoreRecyclerOptions<Apartment> options = new FirestoreRecyclerOptions.Builder<Apartment>()
                .setQuery(query, Apartment.class)
                .setLifecycleOwner(this)
                .build();
        // ------
        recyclerAdapter = new ApartmentAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(getContext()));
        recyclerView.setAdapter(recyclerAdapter);
    }

    /*private RangeSlider.OnSliderTouchListener getTouchListener(String field) {
        return new RangeSlider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                updateRecyclerView(field, null, slider.getValues().get(0), slider.getValues().get(1));
            }
        };
    }*/

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
