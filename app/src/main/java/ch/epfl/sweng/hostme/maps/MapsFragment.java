package ch.epfl.sweng.hostme.maps;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.ui.IOnBackPressed;

public class MapsFragment extends Fragment implements IOnBackPressed, OnMapReadyCallback {

    private View root;
    private String fullAddress;

    public MapsFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.maps, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.fullAddress = bundle.getString("address");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        return root;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
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
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

