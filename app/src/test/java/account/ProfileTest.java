package account;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.sweng.hostme.account.Profile;

public class ProfileTest {

    @Test
    public void getFieldsTest() {

        Profile user = new Profile("Joe", "Test", "joe@gmail.com", "Male", "EPFL");

        assertEquals("Joe", user.getFirstName());
        assertEquals("Test", user.getLastName());
        assertEquals("joe@gmail.com", user.getEmail());
        assertEquals("Male", user.getGender());
    }

    @Test
    public void setFieldsTest() {

        Profile user = new Profile("Joe", "Test", "joe@gmail.com", "Male", "UNIL");

        assertEquals("Joe", user.getFirstName());
        assertEquals("Test", user.getLastName());
        assertEquals("joe@gmail.com", user.getEmail());
        assertEquals("Male", user.getGender());

        user.setFirstName("Fred");
        user.setLastName("Test2");
        user.setEmail("fred@gmail.com");
        user.setGender("Female");

        assertEquals("Fred", user.getFirstName());
        assertEquals("Test2", user.getLastName());
        assertEquals("fred@gmail.com", user.getEmail());
        assertEquals("Female", user.getGender());

    }

}
