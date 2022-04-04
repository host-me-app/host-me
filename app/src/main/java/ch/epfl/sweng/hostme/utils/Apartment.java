package ch.epfl.sweng.hostme.utils;

import java.util.Date;

public class Apartment {

    private String city;
    private int npa;
    private String address;
    private int area;
    private boolean available;
    private Date currentLease;
    private String bath;
    private int deposit;
    private boolean furnished;
    private String image_path;
    private String kitchen;
    private String laundry;
    private String lid;
    private String name;
    private int occupants;
    private boolean pets;
    private String proprietor;
    private int rent;
    private String room;
    private int utilities;
    private String uid;

    /**
     * constructor needed for Firebase
     */
    public Apartment() {
    }

    /**
     * Constructor of a an apartment
     *
     * @param city
     * @param npa
     * @param address
     * @param area
     * @param available
     * @param currentLease
     * @param bath
     * @param deposit
     * @param furnished
     * @param image_path
     * @param kitchen
     * @param laundry
     * @param lid
     * @param name
     * @param occupants
     * @param pets
     * @param proprietor
     * @param rent
     * @param room
     * @param utilities
     * @param uid
     */
    public Apartment(String city, int npa, String address, int area, boolean available, Date currentLease, String bath, int deposit,
                     boolean furnished, String image_path, String kitchen, String laundry, String lid,
                     String name, int occupants, boolean pets, String proprietor, int rent, String room,
                     int utilities, String uid) {
        this.city = city;
        this.npa = npa;
        this.address = address;
        this.area = area;
        this.available = available;
        this.currentLease = currentLease;
        this.bath = bath;
        this.deposit = deposit;
        this.furnished = furnished;
        this.image_path = image_path;
        this.kitchen = kitchen;
        this.laundry = laundry;
        this.lid = lid;
        this.name = name;
        this.occupants = occupants;
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

    public Date getCurrentLease() {
        return currentLease;
    }

    public void setCurrentLease(Date currentLease) {
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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
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

    public int getOccupants() {
        return occupants;
    }

    public void setOccupants(int occupants) {
        this.occupants = occupants;
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
}