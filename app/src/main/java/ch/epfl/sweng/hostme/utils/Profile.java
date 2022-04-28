package ch.epfl.sweng.hostme.utils;

import java.util.Objects;

public class Profile {

    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String school;

    public Profile() {

    }

    public Profile(String firstName, String lastName, String email, String gender, String school) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.school = school;
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

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return firstName.equals(profile.firstName) && lastName.equals(profile.lastName) && gender.equals(profile.gender) && email.equals(profile.email);
    }

}
