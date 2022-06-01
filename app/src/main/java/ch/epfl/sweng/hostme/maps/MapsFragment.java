package ch.epfl.sweng.hostme.maps;

import static ch.epfl.sweng.hostme.utils.Constants.ADDRESS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_USERS;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.ui.IOnBackPressed;
import ch.epfl.sweng.hostme.utils.Profile;

public class MapsFragment extends Fragment implements IOnBackPressed, OnMapReadyCallback {

    HashMap<String, LatLng> schools = new HashMap<>();
    Button dailyRoute;
    private String fullAddress;
    private ProgressBar progressBar;

    public MapsFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.maps, container, false);
        this.progressBar = root.findViewById(R.id.progress_map);
        this.dailyRoute = root.findViewById(R.id.daily_route_maps);
        loading(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.fullAddress = bundle.getString(ADDRESS);
        }

        this.schools.put("EPFL", new LatLng(46.5197, 6.5657));
        this.schools.put("EHL", new LatLng(46.5604818, 6.6827063));
        this.schools.put("CHUV", new LatLng(46.5253, 6.6422));
        this.schools.put("UNIL", new LatLng(46.52136915, 6.574215492));

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        return root;
    }

    /**
     * display a progress bar during the call to the DB
     *
     * @param isLoading if true show the progress bar otherwise hide
     */
    private void loading(Boolean isLoading) {
        if (isLoading) {
            this.dailyRoute.setVisibility(View.GONE);
            this.progressBar.setVisibility(View.VISIBLE);
        } else {
            this.dailyRoute.setVisibility(View.VISIBLE);
            this.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        DocumentReference docRef = Database.getCollection(KEY_COLLECTION_USERS).document(Auth.getUid());
        docRef.get().addOnSuccessListener(result -> {
            loading(false);
            Profile dbProfile = result.toObject(Profile.class);
            String school = Objects.requireNonNull(dbProfile).getSchool();
            Geocoder coder = new Geocoder(this.getContext());
            List<Address> address;
            try {
                address = coder.getFromLocationName(this.fullAddress, 1);
                Address location = address.get(0);
                LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition pos = new CameraPosition.Builder().target(latlng)
                        .zoom(12.5f)
                        .build();
                googleMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title("Your future home!"));
                if (!school.equals("NONE")) {
                    LatLng schoolCoordinates = this.schools.get(school);
                    assert schoolCoordinates != null;
                    googleMap.addMarker(new MarkerOptions()
                            .position(schoolCoordinates)
                            .title(school));
                }
                this.dailyRoute.setOnClickListener(view -> {
                            String url = "geo:0,0?q=" + latlng.latitude + "," + latlng.longitude + "(Your future home)";
                            Uri gmmIntentUri = Uri.parse(url);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                );
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

