package ch.epfl.sweng.hostme;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import ch.epfl.sweng.hostme.utils.Privacy;

public class Listing {
    private final String ID;
    private String name;
    private String room;
    private String address;
    private double rent;
    private int occupants;
    private int area;
    private boolean furnished;
    private String bath;
    private String kitchen;
    private String laundry;
    private boolean pet;
    private String imageDir;
    // private List<String> images;    // either string of image directory path or change to: <Bitmap | Drawable | Image>
    private boolean available;
    private String proprietor;
    private final String UID;
    private double utilities;
    private double deposit;
    private int duration;
    private Timestamp currentLease;

    private Map<String, String> opt;
    // private Map<String, String> custom;



    public Listing (JSONObject fields) {
        // this.images = new ArrayList<>();
        this.opt = new HashMap<>();
        try {
            this.ID = fields.getString("lid");
            this.name = fields.getString("name");
            this.room = fields.getString("room");
            this.address = fields.getString("address");
            this.rent = fields.getDouble("rent");
            this.occupants = fields.getInt("occupants");
            this.area = fields.getInt("area");
            this.furnished = fields.getBoolean("furnished");
            this.bath = fields.getString("bath");
            this.kitchen = fields.getString("kitchen");
            this.laundry = fields.getString("laundry");
            this.pet = fields.getBoolean("pet");
            this.imageDir = fields.getString("imageDir");
            this.available = fields.getBoolean("available");
            this.proprietor = fields.getString("proprietor");
            this.UID = fields.getString("uid");
            this.utilities = fields.getDouble("utilities");
            this.deposit = fields.getDouble("deposit");
            this.duration = fields.getInt("duration");


            // TODO: pull and parse image directory
            DateFormat time = new SimpleDateFormat();
            this.currentLease = new Timestamp(time.parse(fields.getString("currentLease"),
                    new ParsePosition(0)));
        } catch (org.json.JSONException e) {
            throw new RuntimeException(e);
        }
        JSONObject extra = fields.optJSONObject("opt");
        if (extra != null) {
            try {
                for (Iterator<String> key = extra.keys(); key.hasNext();) {
                    String it = key.next();
                    this.opt.put(it, extra.getString(it));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public final String getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public String getRoom() {
        return this.room;
    }

    public String getAddress() {
        return this.address;
    }

    public double getRent() {
        return this.rent;
    }

    public int getOccupants() {
        return this.occupants;
    }

    public int getArea() {
        return this.area;
    }

    public boolean isFurnished() {
        return this.furnished;
    }

    public String getBath() {
        return this.bath;
    }

    public String getKitchen() {
        return this.kitchen;
    }

    public String getLaundry() {
        return this.laundry;
    }

    public boolean isPet() {
        return this.pet;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public String getProprietor() {
        return this.proprietor;
    }

    public final String getUID() {
        return this.UID;
    }

    public double getUtilities() {
        return this.utilities;
    }

    public double getDeposit() {
        return this.deposit;
    }

    public int getDuration() {
        return this.duration;
    }

    public Timestamp getCurrentLease() {
        return this.currentLease;
    }

    public Map<String, String> getOpt() {
        return this.opt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public void setOccupants(int occupants) {
        this.occupants = occupants;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public void toggleFurnished() {
        this.furnished = !this.furnished;
    }

    public void setBath(Privacy bath) {
        this.bath = bath.toString();
    }

    public void setKitchen(Privacy kitchen) {
        this.kitchen = kitchen.toString();
    }

    public void setLaundry(Privacy laundry) {
        this.laundry = laundry.toString();
    }

    public void togglePet() {
        this.pet = !this.pet;
    }

    public void toggleAvailable() {
        this.available = !this.available;
    }

    public void setProprietor(String proprietor) {
        this.proprietor = proprietor;
    }

    public void setUtilities(double utilities) {
        this.utilities = utilities;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCurrentLease(Timestamp currentLease) {
        this.currentLease = currentLease;
    }

    public JSONObject exportDoc() {
        JSONObject ret = new JSONObject();
        try {
            ret.put("lid", this.ID);
            ret.put("name", this.name);
            ret.put("room", this.room);
            ret.put("address", this.address);
            ret.put("rent", this.rent);
            ret.put("occupants", this.occupants);
            ret.put("area", this.area);
            ret.put("furnished", this.furnished);
            ret.put("bath", this.bath);
            ret.put("kitchen", this.kitchen);
            ret.put("laundry", this.laundry);
            ret.put("pet", this.pet);
            ret.put("imageDir", this.imageDir);
            ret.put("available", this.available);
            ret.put("proprietor", this.proprietor);
            ret.put("uid", this.UID);
            ret.put("utilities", this.utilities);
            ret.put("deposit", this.deposit);
            ret.put("duration", this.duration);
            ret.put("currentLease", this.currentLease);

            for (String it: opt.keySet()) {
                ret.putOpt("opt." + it, opt.get(it));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }
}
