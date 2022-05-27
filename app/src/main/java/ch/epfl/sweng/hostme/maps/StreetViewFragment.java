package ch.epfl.sweng.hostme.maps;

import static ch.epfl.sweng.hostme.utils.Constants.ADDRESS;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewSource;

import java.util.List;
import java.util.Objects;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.ui.IOnBackPressed;

public class StreetViewFragment extends Fragment implements IOnBackPressed, OnStreetViewPanoramaReadyCallback {

    private String fullAddress;
    private SensorManager sensorManager;
    private Sensor orientationSensor;
    private SensorEventListener gyroscopeEventListener;
    private StreetViewPanorama streetViewPan;
    private float tilt;

    public StreetViewFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.street_view, container, false);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.fullAddress = bundle.getString(ADDRESS);
        }

        SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment) getChildFragmentManager().findFragmentById(R.id.street_view_panorama);
        Objects.requireNonNull(streetViewPanoramaFragment).getStreetViewPanoramaAsync(this);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        tilt = 0;

        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                    float angle = -(sensorEvent.values[1] + 90f);
                    tilt = Math.abs(angle) > 90f ? tilt : angle;
                    if (streetViewPan != null) {
                        StreetViewPanoramaCamera previous = streetViewPan.getPanoramaCamera();
                        StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder(previous)
                                .tilt(tilt)
                                .bearing(sensorEvent.values[0])
                                .build();
                        streetViewPan.animateTo(camera, 0);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        return root;
    }

    @Override
    public void onStreetViewPanoramaReady(@NonNull StreetViewPanorama streetViewPanorama) {
        streetViewPan = streetViewPanorama;
        Geocoder coder = new Geocoder(this.getContext());
        List<Address> address;
        try {
            address = coder.getFromLocationName(this.fullAddress, 1);
            Address location = address.get(0);
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            streetViewPan.setPosition(latlng, StreetViewSource.OUTDOOR);
            streetViewPan.setZoomGesturesEnabled(false);
            streetViewPan.setPanningGesturesEnabled(false);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroscopeEventListener, orientationSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroscopeEventListener);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

