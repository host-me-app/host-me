package ch.epfl.sweng.hostme.utils;

import android.graphics.Bitmap;

import com.google.firebase.Timestamp;

public class Apartment {

    private Bitmap bitmap;
    private boolean isFavorite;
    private String docID;
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
    private String lid;
    private int utilities;
    private int deposit;
    private Timestamp currentLease;

    /**
     * constructor needed for Firebase
     */
    public Apartment() {
    }

    /**
     * Constructor of a an apartment
     *
     * @param bitmap
     * @param isFavorite
     * @param docID
     * @param city
     * @param npa
     * @param address
     * @param area
     * @param available
     * @param currentLease
     * @param bath
     * @param deposit
     * @param furnished
     * @param imagePath
     * @param kitchen
     * @param laundry
     * @param lid
     * @param name
     * @param beds
     * @param pets
     * @param proprietor
     * @param rent
     * @param room
     * @param utilities
     * @param uid
     */
    public Apartment(Bitmap bitmap, boolean isFavorite, String docID, String city, int npa, String address,
                     int area, boolean available, Timestamp currentLease, String bath, int deposit,
                     boolean furnished, String imagePath, String kitchen, String laundry, String lid,
                     String name, int beds, boolean pets, String proprietor, int rent, String room,
                     int utilities, String uid) {
        this.bitmap = bitmap;
        this.isFavorite = isFavorite;
        this.docID = docID;
        this.city = city;
        this.npa = npa;
        this.address = address;
        this.area = area;
        this.available = available;
        this.currentLease = currentLease;
        this.bath = bath;
        this.deposit = deposit;
        this.furnished = furnished;
        this.imagePath = imagePath;
        this.kitchen = kitchen;
        this.laundry = laundry;
        this.lid = lid;
        this.name = name;
        this.beds = beds;
        this.pets = pets;
        this.proprietor = proprietor;
        this.rent = rent;
        this.room = room;
        this.utilities = utilities;
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Timestamp getCurrentLease() {
        return currentLease;
    }

    public void setCurrentLease(Timestamp currentLease) {
        this.currentLease = currentLease;
    }

    public String getBath() {
        return bath;
    }

    public void setBath(String bath) {
        this.bath = bath;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public boolean isFurnished() {
        return furnished;
    }

    public void setFurnished(boolean furnished) {
        this.furnished = furnished;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getKitchen() {
        return kitchen;
    }

    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }

    public String getLaundry() {
        return laundry;
    }

    public void setLaundry(String laundry) {
        this.laundry = laundry;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public boolean isPets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    public String getProprietor() {
        return proprietor;
    }

    public void setProprietor(String proprietor) {
        this.proprietor = proprietor;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getUtilities() {
        return utilities;
    }

    public void setUtilities(int utilities) {
        this.utilities = utilities;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNpa() {
        return npa;
    }

    public void setNpa(int npa) {
        this.npa = npa;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}