package ch.epfl.sweng.hostme.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.sweng.hostme.utils.EmailValidator;

public class EmailValidatorTest {

    EmailValidator validator = new EmailValidator();

    @Test
    public void checkEmailIsCorrect() {
        String email = "test@gmail.com";
        assertEquals(true, validator.isValid(email));
    }

    @Test
    public void checkPatternIsIncorrect() {
        String email = "test@gmail.com.";
        assertEquals(false, validator.isValid(email));
        String email2 = ".test@gmail.com";
        assertEquals(false, validator.isValid(email2));
        String email3 = "testgmail.com";
        assertEquals(false, validator.isValid(email3));
        String email4 = "çtêst@gmail.com";
        assertEquals(false, validator.isValid(email4));
    }

}
