package ch.epfl.sweng.hostme;

import java.util.Date;

public class Apartment {

    private String address;
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

    public Apartment() {}

    public Apartment(String address, boolean available, String bath, Date currentLease, int deposit,
                     boolean furnished, String image_path, String kitchen, String laundry, String lid,
                     String name, int occupants, boolean pets, String proprietor, int rent, String room, int utilities) {
        this.address = address;
        this.available = available;
        this.bath = bath;
        this.currentLease = currentLease;
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
    }


    public String getAddress() {
        return address;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getBath() {
        return bath;
    }

    public Date getCurrentLease() {
        return currentLease;
    }

    public int getDeposit() {
        return deposit;
    }

    public boolean isFurnished() {
        return furnished;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getKitchen() {
        return kitchen;
    }

    public String getLaundry() {
        return laundry;
    }

    public String getLid() {
        return lid;
    }

    public String getName() {
        return name;
    }

    public int getOccupants() {
        return occupants;
    }

    public boolean isPets() {
        return pets;
    }

    public String getProprietor() {
        return proprietor;
    }

    public int getRent() {
        return rent;
    }

    public String getRoom() {
        return room;
    }

    public int getUtilities() {
        return utilities;
    }

}
