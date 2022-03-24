package ch.epfl.sweng.hostme;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import ch.epfl.sweng.hostme.utils.Profile;

public class ProfileTest {

    @Test
    public void getFieldsTest(){

        Profile user = new Profile("Joe","Test","joe@gmail.com","Male");

        assertEquals("Joe",user.getFirstName());
        assertEquals("Test",user.getLastName());
        assertEquals("joe@gmail.com",user.getEmail());
        assertEquals("Male",user.getGender());
    }

    @Test
    public void setFieldsTest(){

        Profile user = new Profile("Joe","Test","joe@gmail.com","Male");

        assertEquals("Joe",user.getFirstName());
        assertEquals("Test",user.getLastName());
        assertEquals("joe@gmail.com",user.getEmail());
        assertEquals("Male",user.getGender());

        user.setFirstName("Fred");
        user.setLastName("Test2");
        user.setEmail("fred@gmail.com");
        user.setGender("Female");


        assertEquals("Fred",user.getFirstName());
        assertEquals("Test2",user.getLastName());
        assertEquals("fred@gmail.com",user.getEmail());
        assertEquals("Female",user.getGender());

    }

}