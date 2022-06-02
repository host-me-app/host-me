package ch.epfl.sweng.hostme.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Location {

    public static boolean checkPositionAroundLocation(Context context, String fullAddress, double latitude, double longitude, float radius) {
        float radiusMeters = radius * 1000;
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        try {
            address = coder.getFromLocationName(fullAddress, 1);
            Address location = address.get(0);
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            double earthRadius = 6371000;
            double phyLoc = latitude * Math.PI / 180;
            double phyApa = latlng.latitude * Math.PI / 180;
            double deltaPhy = (latlng.latitude - latitude) * Math.PI / 180;
            double deltaLambda = (latlng.longitude - longitude) * Math.PI / 180;

            double sinDeltaPhy = Math.sin(deltaPhy / 2);
            double sinDeltaLambda = Math.sin(deltaLambda / 2);
            double a = sinDeltaPhy * sinDeltaPhy + Math.cos(phyLoc) * Math.cos(phyApa) * sinDeltaLambda * sinDeltaLambda;
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double dist = earthRadius * c;
            return radiusMeters >= dist;
        } catch (Exception ignored) {
        }
        return false;
    }
}
