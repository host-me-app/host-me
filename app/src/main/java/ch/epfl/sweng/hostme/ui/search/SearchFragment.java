package ch.epfl.sweng.hostme.ui.search;

import static ch.epfl.sweng.hostme.utils.Constants.ADDRESS;
import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.CITY;
import static ch.epfl.sweng.hostme.utils.Constants.FAVORITE_FRAGMENT;
import static ch.epfl.sweng.hostme.utils.Constants.FILTERS;
import static ch.epfl.sweng.hostme.utils.Constants.IS_FAVORITE;
import static ch.epfl.sweng.hostme.utils.Constants.IS_FROM_FILTERS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_FAV;
import static ch.epfl.sweng.hostme.utils.Constants.MAX_AREA;
import static ch.epfl.sweng.hostme.utils.Constants.MAX_PRICE;
import static ch.epfl.sweng.hostme.utils.Constants.NPA;
import static ch.epfl.sweng.hostme.utils.Constants.PERMISSION_ID;
import static ch.epfl.sweng.hostme.utils.Constants.RENT;
import static ch.epfl.sweng.hostme.utils.Location.checkPositionAroundLocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.utils.Apartment;

public class SearchFragment extends Fragment {

    private final static CollectionReference favReference = Database.getCollection(KEY_COLLECTION_FAV);
    private final CollectionReference reference = Database.getCollection(APARTMENTS);
    private SharedPreferences.Editor editor;
    private Button filterButt;
    private boolean filterIsClicked;
    private LinearLayout radiusGpsLayout;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch gpsSwitch;
    private RangeSlider rangeBarGps;
    private RangeSlider rangeBarPrice;
    private RangeSlider rangeBarArea;
    private LinearLayout filters;
    private ArrayList<Apartment> apartments;
    private String searchText;
    private RecyclerView recyclerView;
    private Button clearFilters;
    private FusedLocationProviderClient fusedLocationClient;
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            filterLocation(mLastLocation);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity());
        this.radiusGpsLayout = root.findViewById(R.id.filter_gps);
        this.gpsSwitch = root.findViewById(R.id.gps_switch);
        this.recyclerView = root.findViewById(R.id.search_recycler_view);
        this.rangeBarGps = root.findViewById(R.id.range_bar_gps);
        this.rangeBarPrice = root.findViewById(R.id.range_bar_price);
        this.rangeBarArea = root.findViewById(R.id.range_bar_area);
        this.filterButt = root.findViewById(R.id.filters);
        this.filters = root.findViewById(R.id.all_filters);
        this.clearFilters = root.findViewById(R.id.clear_filters);
        this.clearFilters.setVisibility(View.GONE);

        SearchView searchView = root.findViewById(R.id.search_view);

        this.apartments = new ArrayList<>();
        this.editor = root.getContext().getSharedPreferences(FILTERS, Context.MODE_PRIVATE).edit();
        this.setUpRecyclerView();

        this.gpsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                this.radiusGpsLayout.setVisibility(View.VISIBLE);
            } else {
                this.radiusGpsLayout.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                editor.putBoolean(IS_FROM_FILTERS, true).apply();
                searchText = s.toLowerCase();
                if (gpsSwitch.isChecked()) {
                    getLastLocation();
                } else {
                    updateRecyclerView(null);
                }
                editor.putBoolean(IS_FROM_FILTERS, false).apply();
                return true;
            }
        });

        this.filterIsClicked = false;
        this.filterButt.setOnClickListener(view -> changeFiltersViewAndUpdate());
        this.clearFilters.setOnClickListener(view -> {
            searchView.setQuery("", false);
            searchView.clearFocus();
            setRangeBar();
        });

        this.changeFilterVisibility(View.GONE);
        this.setRangeBar();

        favReference.document(Auth.getUid()).addSnapshotListener((value, error) -> {
            SharedPreferences pref = root.getContext().getSharedPreferences(FAVORITE_FRAGMENT, Context.MODE_PRIVATE);
            if (value != null && value.exists() && pref.getBoolean(IS_FAVORITE, false)) {
                setUpRecyclerView();
            }
        });
        return root;
    }

    /**
     * set the range bar to default values
     */
    private void setRangeBar() {
        this.gpsSwitch.setChecked(false);
        this.rangeBarPrice.setValues(0f, MAX_PRICE);
        this.rangeBarPrice.setLabelFormatter(value -> value + " CHF");
        this.rangeBarArea.setValues(0f, MAX_AREA);
        this.rangeBarArea.setLabelFormatter(value -> value + " m²");
    }

    /**
     * Close or open the filters and update if necessary
     */
    private void changeFiltersViewAndUpdate() {
        if (!this.filterIsClicked) {
            this.clearFilters.setVisibility(View.VISIBLE);
            this.changeFilterVisibility(View.VISIBLE);
            this.filterButt.setText(R.string.apply_filters);
            this.filterIsClicked = true;
        } else {
            this.clearFilters.setVisibility(View.GONE);
            this.changeFilterVisibility(View.GONE);
            this.filterButt.setText(R.string.filters_text);
            this.filterIsClicked = false;
            if (this.gpsSwitch.isChecked()) {
                this.getLastLocation();
            } else {
                this.editor.putBoolean(IS_FROM_FILTERS, true).apply();
                this.updateRecyclerView(null);
                this.editor.putBoolean(IS_FROM_FILTERS, false).apply();
            }
        }
    }

    /**
     * set up or update the recycler view
     */
    private void updateRecyclerView(Location location) {
        this.apartments = new ArrayList<>();
        float radius = this.rangeBarGps.getValues().get(0);
        float min = this.rangeBarPrice.getValues().get(0);
        float max = this.rangeBarPrice.getValues().get(1);
        float min2 = this.rangeBarArea.getValues().get(0);
        float max2 = this.rangeBarArea.getValues().get(1);
        this.reference.get().addOnSuccessListener(result -> {
            this.apartments.clear();
            double latitude = 0;
            double longitude = 0;
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            for (DocumentSnapshot doc : result.getDocuments()) {
                Apartment apartment = doc.toObject(Apartment.class);
                long rent = Objects.requireNonNull(doc.getLong(RENT));
                long area = Objects.requireNonNull(doc.getLong(AREA));
                String city = Objects.requireNonNull(doc.getString(CITY));
                String address = Objects.requireNonNull(doc.getString(ADDRESS));
                long npa = Objects.requireNonNull(doc.getLong(NPA));
                String fullAddress = address + " " + city + " " + npa;
                if ((min <= rent) && (rent <= max) && (min2 <= area) && (area <= max2)) {
                    if ((this.searchText == null) || (String.valueOf(npa).toLowerCase().contains(this.searchText) ||
                            Objects.requireNonNull(address).toLowerCase().contains(this.searchText) || Objects.requireNonNull(city).toLowerCase().contains(searchText))) {
                        if (location == null || checkPositionAroundLocation(this.getContext(), fullAddress, latitude, longitude, radius)) {
                            this.apartments.add(apartment);
                        }
                    }
                }
            }
            this.displayRecycler();
        });
    }

    /**
     * Initialize the recycler view with no filtered apartments
     */
    private void setUpRecyclerView() {
        this.editor.putBoolean(IS_FROM_FILTERS, false);
        this.editor.apply();
        this.apartments = new ArrayList<>();
        this.reference.get().addOnSuccessListener(result -> {
            this.apartments.clear();
            for (DocumentSnapshot doc : result.getDocuments()) {
                Apartment apartment = doc.toObject(Apartment.class);
                this.apartments.add(apartment);
            }
            displayRecycler();
        });
    }

    /**
     * Set up and display the recycler view with the apartments
     */
    private void displayRecycler() {
        List<Apartment> apartmentsWithoutDuplicate = new ArrayList<>(new HashSet<>(apartments));
        ApartmentAdapter recyclerAdapter = new ApartmentAdapter(apartmentsWithoutDuplicate);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setItemViewCacheSize(20);
        this.recyclerView.setDrawingCacheEnabled(true);
        this.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        this.recyclerView.setAdapter(recyclerAdapter);
    }


    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (this.checkPermissions()) {
            if (this.isLocationEnabled()) {
                this.fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location == null) {
                        LocationRequest locationRequest = new LocationRequest();
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationRequest.setInterval(5);
                        locationRequest.setFastestInterval(0);
                        locationRequest.setNumUpdates(1);
                        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity());
                        this.fusedLocationClient.requestLocationUpdates(locationRequest, this.locationCallback, Looper.myLooper());
                    } else {
                        this.filterLocation(location);
                    }
                });
            }
        } else {
            this.requestPermissionsForLocation();
        }
    }

    private void requestPermissionsForLocation() {
        requestPermissions(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) this.requireActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void filterLocation(Location location) {
        this.updateRecyclerView(location);
    }

    /**
     * display or not all the filters
     *
     * @param visible GONE or INVISIBLE
     */
    private void changeFilterVisibility(int visible) {
        this.filters.setVisibility(visible);
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
