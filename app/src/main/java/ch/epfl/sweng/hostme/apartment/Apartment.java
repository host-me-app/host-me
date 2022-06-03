package ch.epfl.sweng.hostme.apartment;

import static ch.epfl.sweng.hostme.utils.Constants.ADDRESS;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.BATH;
import static ch.epfl.sweng.hostme.utils.Constants.BEDS;
import static ch.epfl.sweng.hostme.utils.Constants.CITY;
import static ch.epfl.sweng.hostme.utils.Constants.DEPOSIT;
import static ch.epfl.sweng.hostme.utils.Constants.DURATION;
import static ch.epfl.sweng.hostme.utils.Constants.FURNISHED;
import static ch.epfl.sweng.hostme.utils.Constants.IMAGE_PATH;
import static ch.epfl.sweng.hostme.utils.Constants.KITCHEN;
import static ch.epfl.sweng.hostme.utils.Constants.LAUNDRY;
import static ch.epfl.sweng.hostme.utils.Constants.NAME;
import static ch.epfl.sweng.hostme.utils.Constants.NPA;
import static ch.epfl.sweng.hostme.utils.Constants.PETS;
import static ch.epfl.sweng.hostme.utils.Constants.PROPRIETOR;
import static ch.epfl.sweng.hostme.utils.Constants.RENT;
import static ch.epfl.sweng.hostme.utils.Constants.ROOM;
import static ch.epfl.sweng.hostme.utils.Constants.UID;
import static ch.epfl.sweng.hostme.utils.Constants.UTILITIES;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

public class Apartment {
    private String name;
    private String room;
    private String address;
    private int npa;
    private String city;
    private int rent;
    private int beds;
    private int area;
    private boolean furnished;
    private String bath;
    private String kitchen;
    private String laundry;
    private boolean pets;
    private String imagePath;
    private boolean available;
    private String proprietor;
    private String uid;
    private int utilities;
    private int deposit;
    private int duration;
    private Timestamp currentLease;
    private String docId;
    private Bitmap image;

    public Apartment() {

    }

    /**
     * Constructor of a residence listing. Stores all provided fields, sets default values for
     * fields that do not exist at instantiation, and checks for optional values.
     *
     * @param fields a JSONObject containing all required fields of the Add UI, optionally
     *               containing some additional fields
     */
    public Apartment(JSONObject fields) {
        try {
            this.name = fields.getString(NAME);
            this.room = fields.getString(ROOM);
            this.address = fields.getString(ADDRESS);
            this.npa = fields.getInt(NPA);
            this.city = fields.getString(CITY);
            this.rent = fields.getInt(RENT);
            this.beds = fields.getInt(BEDS);
            this.area = fields.getInt(AREA);
            this.furnished = fields.getBoolean(FURNISHED);
            this.bath = fields.getString(BATH);
            this.kitchen = fields.getString(KITCHEN);
            this.laundry = fields.getString(LAUNDRY);
            this.pets = fields.getBoolean(PETS);
            this.imagePath = fields.getString(IMAGE_PATH);
            this.proprietor = fields.getString(PROPRIETOR);
            this.uid = fields.getString(UID);
            this.utilities = fields.getInt(UTILITIES);
            this.deposit = fields.getInt(DEPOSIT);
            this.duration = fields.getInt(DURATION);

            this.available = true;
            this.currentLease = null;
        } catch (JSONException ignored) {
        }
    }

    /**
     * get name
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * set name
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get room
     * @return room
     */
    public String getRoom() {
        return this.room;
    }

    /**
     * set room
     * @param room new room
     */
    public void setRoom(String room) {
        this.room = room;
    }

    /**
     * get address
     * @return address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * set address
     * @param address new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * get npa
     * @return npa
     */
    public int getNpa() {
        return this.npa;
    }

    /**
     * set npa
     * @param npa new npa
     */
    public void setNpa(int npa) {
        this.npa = npa;
    }

    /**
     * get city
     * @return city
     */
    public String getCity() {
        return this.city;
    }

    /**
     * set city
     * @param city new city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * get rent
     * @return rent
     */
    public int getRent() {
        return this.rent;
    }

    /**
     * set rent
     * @param rent new rent
     */
    public void setRent(int rent) {
        this.rent = rent;
    }

    /**
     * get beds
     * @return beds
     */
    public int getBeds() {
        return this.beds;
    }

    /**
     * set number of beds
     * @param beds new number of beds
     */
    public void setBeds(int beds) {
        this.beds = beds;
    }

    /**
     * get area
     * @return area
     */
    public int getArea() {
        return this.area;
    }

    /**
     * set area
     * @param area new area
     */
    public void setArea(int area) {
        this.area = area;
    }

    /**
     * get furnished boolean
     * @return furnished
     */
    public boolean isFurnished() {
        return this.furnished;
    }

    /**
     * set the furnished to the contrary
     * @return contrary of the current furnished boolean value
     */
    public void toggleFurnished() {
        this.furnished = !this.furnished;
    }

    /**
     * get bath
     * @return bath
     */
    public String getBath() {
        return this.bath;
    }

    /**
     * set bath
     * @param bath new bath
     */
    public void setBath(String bath) {
        this.bath = bath;
    }

    /**
     * get kitchen
     * @return kitchen
     */
    public String getKitchen() {
        return this.kitchen;
    }

    /**
     * set kitchen
     * @param kitchen new kitchen
     */
    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }

    /**
     * get laundry
     * @return laundry
     */
    public String getLaundry() {
        return this.laundry;
    }

    /**
     * set laundry
     * @param laundry new laundry
     */
    public void setLaundry(String laundry) {
        this.laundry = laundry;
    }

    /**
     * get if pets are allowed
     * @return pets boolean
     */
    public boolean isPets() {
        return this.pets;
    }

    /**
     * change the pets allowed boolean value
     * @return pets boolean
     */
    public void togglePets() {
        this.pets = !this.pets;
    }

    /**
     * get image path
     * @return imagePath
     */
    public String getImagePath() {
        return this.imagePath;
    }

    /**
     * get availability
     * @return availability
     */
    public boolean isAvailable() {
        return this.available;
    }

    /**
     * change the available boolean value
     * @return opposite value of availability
     */
    public void toggleAvailable() {
        this.available = !this.available;
    }

    /**
     * get proprietor
     * @return proprietor
     */
    public String getProprietor() {
        return this.proprietor;
    }

    /**
     * set proprietor
     * @param proprietor new proprietor
     */
    public void setProprietor(String proprietor) {
        this.proprietor = proprietor;
    }

    /**
     * get user id
     * @return user id
     */
    public String getUid() {
        return this.uid;
    }

    /**
     * get utilities
     * @return utilities
     */
    public int getUtilities() {
        return this.utilities;
    }

    /**
     * set utilities
     * @param utilities new utilities
     */
    public void setUtilities(int utilities) {
        this.utilities = utilities;
    }

    /**
     * get deposit
     * @return deposit
     */
    public int getDeposit() {
        return this.deposit;
    }

    /**
     * set deposit
     * @param deposit new deposit
     */
    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    /**
     * get duration
     * @return duration
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * set duration
     * @param duration new duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * get current lease
     * @return current lease
     */
    public Timestamp getCurrentLease() {
        return this.currentLease;
    }

    /**
     * set current lease
     * @param currentLease new current lease
     */
    public void setCurrentLease(Timestamp currentLease) {
        this.currentLease = currentLease;
    }

    /**
     * get document id
     * @return document id
     */
    public String getDocId() {
        return this.docId;
    }

    /**
     * set document id
     * @param docId new document id
     */
    public void setDocId(String docId) {
        this.docId = docId;
    }

    /**
     * get image
     * @return image
     */
    public Bitmap getImage() {
        return this.image;
    }

    /**
     * set image
     * @param image new image
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * A JSON representation of a Listing that may be used to create identical objects or edit the
     * fields of this Listing.
     *
     * @return JSONObject holding approximate representations of all this Listing's fields
     */
    public JSONObject exportDoc() {
        JSONObject ret = new JSONObject();
        try {
            ret.put(NAME, this.name);
            ret.put(ROOM, this.room);
            ret.put(ADDRESS, this.address);
            ret.put(NPA, this.npa);
            ret.put(CITY, this.city);
            ret.put(RENT, this.rent);
            ret.put(BEDS, this.beds);
            ret.put(AREA, this.area);
            ret.put(FURNISHED, this.furnished);
            ret.put(BATH, this.bath);
            ret.put(KITCHEN, this.kitchen);
            ret.put(LAUNDRY, this.laundry);
            ret.put(PETS, this.pets);
            ret.put(IMAGE_PATH, this.imagePath);
            ret.put("available", this.available);
            ret.put(PROPRIETOR, this.proprietor);
            ret.put(UID, this.uid);
            ret.put(UTILITIES, this.utilities);
            ret.put(DEPOSIT, this.deposit);
            ret.put(DURATION, this.duration);
            ret.put("currentLease", this.currentLease);
            ret.put("docId", this.docId);
        } catch (Exception ignored) {
        }

        return ret;
    }

    @NonNull
    @Override
    public String toString() {
        return this.exportDoc().toString();
    }
}

