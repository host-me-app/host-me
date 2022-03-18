package ch.epfl.sweng.hostme;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

// import com.google.firebase.

public class Listing {
    private String id;
    private String name;
    private String room;
    private String address;
    private double rent;
    private int occupants;
    private boolean furnished;
    private String bathroom;
    private String kitchen;
    private String laundry;
    private boolean pet;
    private List<String> images;    // either strings of image paths or change to: <Bitmap | Drawable | Image>
    private boolean available;
    private String proprietor;
    private double utilities;
    private double deposit;
    private String duration;        // might be converted to integer or timestamp for delta on currentLease
    private String currentLease;    // Timestamp of some kind

    private Map<String, String> opt;
    // private Map<String, String> custom;

    public Listing (JSONObject fields) {
        this.images = new ArrayList<>();
        this.opt = new HashMap<>();
        try {
            this.id = fields.getString("lid");
            this.name = fields.getString("name");
            this.room = fields.getString("room");
            this.address = fields.getString("address");
            this.rent = fields.getDouble("rent");
            this.occupants = fields.getInt("occupants");
            this.furnished = fields.getBoolean("furnished");
            this.bathroom = fields.getString("bathroom");
            this.kitchen = fields.getString("kitchen");
            this.laundry = fields.getString("laundry");
            this.pet = fields.getBoolean("pet");
            this.available = fields.getBoolean("available");
            this.proprietor = fields.getString("proprietor");
            this.utilities = fields.getDouble("utilities");
            this.deposit = fields.getDouble("deposit");
            this.duration = fields.getString("duration");

            JSONArray preview = fields.getJSONArray("images");
            for (int c = 0; c < preview.length(); c++) {
                this.images.add(preview.getString(c));
            }
            // TODO: Extract lease timestamp
        } catch (org.json.JSONException e) {
            throw new RuntimeException(e);
        }

    }

    // TODO: Accessors + JSON export

}
