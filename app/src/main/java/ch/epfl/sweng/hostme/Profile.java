package ch.epfl.sweng.hostme;

import androidx.annotation.Nullable;

public class Profile {

    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phoneNumber;

    public Profile(){};

    public Profile(String firstName, String lastName, String email, String gender, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Profile(Profile that){
        this(that.getFirstName(), that.getLastName(), that.getEmail(), that.getGender(), that.getPhoneNumber());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
