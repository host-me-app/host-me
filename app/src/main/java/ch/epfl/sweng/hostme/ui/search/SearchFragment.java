package ch.epfl.sweng.hostme.ui.search;

import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
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
    public static final String IS_FAVORITE = "isFavorite";
    public static final String FAVORITE_FRAGMENT = "FavoriteFragment";
    private final CollectionReference reference = Database.getCollection(APARTMENTS);
    private final CollectionReference favReference = Database.getCollection("favorite_apart");
    private ApartmentAdapter recyclerAdapter;
    private Button filterButt;
    private boolean filterIsClicked;
    private LinearLayout radiusGpsLayout;
    private Switch gpsSwitch;
    private RangeSlider rangeBarGps;
    private RangeSlider rangeBarPrice;
    private RangeSlider rangeBarArea;
    private LinearLayout filters;
    private ArrayList<Apartment> apartments;
    private String searchText;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private View root;
    private Button clearFilters;
    private FusedLocationProviderClient mFusedLocationClient;
    private int PERMISSION_ID = 44;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.recycler_view, container, false);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity());
        radiusGpsLayout = root.findViewById(R.id.filter_gps);
        gpsSwitch = root.findViewById(R.id.gpsSwitch);
        recyclerView = root.findViewById(R.id.recyclerView);
        rangeBarGps = root.findViewById(R.id.range_bar_gps);
        rangeBarPrice = root.findViewById(R.id.range_bar_price);
        rangeBarArea = root.findViewById(R.id.range_bar_area);
        filterButt = root.findViewById(R.id.filters);
        filters = root.findViewById(R.id.all_filters);
        SearchView searchView = root.findViewById(R.id.search_view);
        clearFilters = root.findViewById(R.id.clear_filters);
        clearFilters.setVisibility(View.GONE);

        gpsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                radiusGpsLayout.setVisibility(View.VISIBLE);
            } else {
                radiusGpsLayout.setVisibility(View.GONE);
            }

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchText = s.toLowerCase();
                if (gpsSwitch.isChecked()) {
                    getLastLocation();
                } else {
                    updateRecyclerView(null, rangeBarGps.getValues().get(0), rangeBarPrice.getValues().get(0), rangeBarPrice.getValues().get(1),
                            rangeBarArea.getValues().get(0), rangeBarArea.getValues().get(1));
                }
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
        gpsSwitch.setChecked(false);
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
            getLastLocation();
            filterButt.setText(R.string.filters_text);
            filterIsClicked = false;
            if (gpsSwitch.isChecked()) {
                getLastLocation();
            } else {
                updateRecyclerView(null, rangeBarGps.getValues().get(0), rangeBarPrice.getValues().get(0), rangeBarPrice.getValues().get(1),
                        rangeBarArea.getValues().get(0), rangeBarArea.getValues().get(1));
            }

        }
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
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location == null) {
                        LocationRequest mLocationRequest = new LocationRequest();
                        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        mLocationRequest.setInterval(5);
                        mLocationRequest.setFastestInterval(0);
                        mLocationRequest.setNumUpdates(1);
                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity());
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    } else {
                        filterLocation(location);
                    }
                });
            }
        } else {
            requestPermissionsForLocation();
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

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            filterLocation(mLastLocation);
        }
    };

    private void filterLocation(Location location) {
        updateRecyclerView(location, rangeBarGps.getValues().get(0), rangeBarPrice.getValues().get(0), rangeBarPrice.getValues().get(1),
                rangeBarArea.getValues().get(0), rangeBarArea.getValues().get(1));
    }

    private boolean checkPositionAroundLocation(String fullAddress, double latitude, double longitude, float radius) {
        if (latitude == 1000) {
            System.out.println("bbbb");
            return true;
        }
        float radiusMeters = radius * 1000;
        Geocoder coder = new Geocoder(this.getContext());
        List<Address> address;
        try {
            address = coder.getFromLocationName(fullAddress, 1);
            Address location = address.get(0);
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            double earthRadius = 6371000;
            double phyLoc = latitude * Math.PI/180;
            double phyApa = latlng.latitude * Math.PI/180;
            double deltaPhy = (latlng.latitude - latitude) * Math.PI/180;
            double deltaLambda = (latlng.longitude - longitude) * Math.PI/180;

            double sinDeltaPhy = Math.sin(deltaPhy/2);
            double sinDeltaLambda = Math.sin(deltaLambda/2);
            double a = sinDeltaPhy * sinDeltaPhy + Math.cos(phyLoc) * Math.cos(phyApa) * sinDeltaLambda * sinDeltaLambda;
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double dist = earthRadius * c;
            return radiusMeters >= dist;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
    private void updateRecyclerView(Location location, float radius, float min, float max, float min2, float max2) {
        apartments = new ArrayList<>();
        reference.get().addOnCompleteListener(task -> {
            apartments.clear();
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                double latitude;
                double longitude;
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                } else {
                    System.out.println("aaaa");
                    latitude = 1000;
                    longitude = 1000;
                }
                for (DocumentSnapshot doc : snapshot.getDocuments()) {
                    Apartment apartment = doc.toObject(Apartment.class);
                    Long rent = (Long) doc.get("rent");
                    Long area = (Long) doc.get("area");
                    String city = (String) doc.get("city");
                    String address = (String) doc.get("address");
                    Long npa = (Long) doc.get("npa");
                    String fullAddress = address + " " + city + " " + npa;
                    apartment.setDocID(doc.getId());
                    if ((min <= rent) && (rent <= max) && (min2 <= area) && (area <= max2) && checkPositionAroundLocation(fullAddress, latitude, longitude, radius)) {
                        if ((searchText == null) || (String.valueOf(npa).toLowerCase().contains(searchText) ||
                                address.toLowerCase().contains(searchText) || city.toLowerCase().contains(searchText))) {
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
