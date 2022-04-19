package ch.epfl.sweng.hostme.maps;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewSource;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.ui.IOnBackPressed;

public class StreetViewFragment extends Fragment implements IOnBackPressed, OnStreetViewPanoramaReadyCallback {

    private View root;
    private String fullAddress;

    // George St, Sydney
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

    // Cole St, San Fran
    private static final LatLng SAN_FRAN = new LatLng(37.769263, -122.450727);

    // Santorini, Greece
    private static final String SANTORINI = "WddsUw1geEoAAAQIt9RnsQ";

    // LatLng with no panorama
    private static final LatLng INVALID = new LatLng(-45.125783, 151.276417);

    /**
     * The amount in degrees by which to scroll the camera
     */
    private static final int PAN_BY_DEG = 30;

    private static final float ZOOM_BY = 0.5f;

    private StreetViewPanorama mStreetViewPanorama;

    private SeekBar mCustomDurationBar;

    public StreetViewFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.street_view, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.fullAddress = bundle.getString("address");
        }

        SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment)
                        getChildFragmentManager().findFragmentById(R.id.street_view_panorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        return root;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        Geocoder coder = new Geocoder(this.getContext());
        List<Address> address;
        try {
            address = coder.getFromLocationName(this.fullAddress, 1);
            Address location = address.get(0);
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            streetViewPanorama.setPosition(latlng, StreetViewSource.OUTDOOR);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

