package ch.epfl.sweng.hostme;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import ch.epfl.sweng.hostme.utils.Privacy;

public class Listing {
    private final static String BASEURL = "projects/869636231621/storage/host-me-9072b.appspot.com/files/apartments%2F";

    private String name;
    private String room;
    private String address;
    private int npa;
    private String city;
    private double rent;
    private int beds;
    private int area;
    private boolean furnished;
    private String bath;
    private String kitchen;
    private String laundry;
    private boolean pet;
    private String imageDir;
    // private List<String> images;    // will change to <Bitmap | Drawable | Image>
    private boolean available;
    private String proprietor;
    private String uid;
    private double utilities;
    private double deposit;
    private int duration;
    private Timestamp currentLease;

    private Map<String, String> opt;
    // private Map<String, String> custom;

    public Listing () { }

    public Listing (JSONObject fields) {
        // this.images = new ArrayList<>();
        this.opt = new HashMap<>();
        try {
            this.name = fields.getString("name");
            this.room = fields.getString("room");
            this.address = fields.getString("address");
            this.npa = fields.getInt("npa");
            this.city = fields.getString("city");
            this.rent = fields.getDouble("rent");
            this.beds = fields.getInt("beds");
            this.area = fields.getInt("area");
            this.furnished = fields.getBoolean("furnished");
            this.bath = fields.getString("bath");
            this.kitchen = fields.getString("kitchen");
            this.laundry = fields.getString("laundry");
            this.pet = fields.getBoolean("pet");
            this.proprietor = fields.getString("proprietor");
            this.uid = fields.getString("uid");
            this.utilities = fields.getDouble("utilities");
            this.deposit = fields.getDouble("deposit");
            this.duration = fields.getInt("duration");

            this.imageDir = String.format("%s%s_%s_%s", BASEURL,
                    this.proprietor.toLowerCase(), this.name.toLowerCase(), this.room.toLowerCase());
            this.available = true;
            this.currentLease = null;

            // TODO: pull and parse image directory
        } catch (org.json.JSONException e) { throw new RuntimeException(e); }
        JSONObject extra = fields.optJSONObject("opt");
        if (extra != null) {
            try {
                for (Iterator<String> key = extra.keys(); key.hasNext();) {
                    String it = key.next();
                    this.opt.put(it, extra.getString(it));
                }
            } catch (JSONException e) { throw new RuntimeException(e); }
        }

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return this.room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNpa() {
        return this.npa;
    }

    public void setNpa(int npa) {
        this.npa = npa;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getRent() {
        return this.rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getBeds() {
        return this.beds;
    }

    public int getArea() {
        return this.area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public boolean isFurnished() {
        return this.furnished;
    }

    public void toggleFurnished() {
        this.furnished = !this.furnished;
    }

    public String getBath() {
        return this.bath;
    }

    public void setBath(Privacy bath) {
        this.bath = bath.toString();
    }

    public String getKitchen() {
        return this.kitchen;
    }

    public void setKitchen(Privacy kitchen) {
        this.kitchen = kitchen.toString();
    }

    public String getLaundry() {
        return this.laundry;
    }

    public void setLaundry(Privacy laundry) {
        this.laundry = laundry.toString();
    }

    public boolean isPet() {
        return this.pet;
    }

    public void togglePet() {
        this.pet = !this.pet;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void toggleAvailable() {
        this.available = !this.available;
    }

    public String getProprietor() {
        return this.proprietor;
    }

    public void setProprietor(String proprietor) {
        this.proprietor = proprietor;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getUtilities() {
        return this.utilities;
    }

    public void setUtilities(double utilities) {
        this.utilities = utilities;
    }

    public double getDeposit() {
        return this.deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Timestamp getCurrentLease() {
        return this.currentLease;
    }

    public void setCurrentLease(Timestamp currentLease) {
        this.currentLease = currentLease;
    }
    public Map<String, String> getOpt() {
        return this.opt;
    }

    public JSONObject exportDoc() {
        JSONObject ret = new JSONObject();
        try {
            ret.put("name", this.name);
            ret.put("room", this.room);
            ret.put("address", this.address);
            ret.put("npa", this.npa);
            ret.put("city", this.city);
            ret.put("rent", this.rent);
            ret.put("beds", this.beds);
            ret.put("area", this.area);
            ret.put("furnished", this.furnished);
            ret.put("bath", this.bath);
            ret.put("kitchen", this.kitchen);
            ret.put("laundry", this.laundry);
            ret.put("pet", this.pet);
            ret.put("imageDir", this.imageDir);
            ret.put("available", this.available);
            ret.put("proprietor", this.proprietor);
            ret.put("uid", this.uid);
            ret.put("utilities", this.utilities);
            ret.put("deposit", this.deposit);
            ret.put("duration", this.duration);
            ret.put("currentLease", this.currentLease);

            for (String it: opt.keySet()) {
                ret.putOpt("opt." + it, opt.get(it));
            }
        } catch (JSONException e) { throw new RuntimeException(e); }

        return ret;
    }
}
