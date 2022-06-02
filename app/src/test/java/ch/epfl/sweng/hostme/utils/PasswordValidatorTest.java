package ch.epfl.sweng.hostme.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.sweng.hostme.creation.PasswordValidator;

public class PasswordValidatorTest {


    @Test
    public void checkPatternIsCorrect() {
        PasswordValidator validator = new PasswordValidator();
        String pwd = "Test111!";
        assertEquals(true, validator.isValid(pwd));
    }

    @Test
    public void checkPatternIsInCorrect() {
        PasswordValidator validator = new PasswordValidator();
        String pwd = "testTest!";
        assertEquals(false, validator.isValid(pwd));
        String pwd2 = "Test1111";
        assertEquals(false, validator.isValid(pwd2));
        String pwd3 = "test111!";
        assertEquals(false, validator.isValid(pwd3));
        String pwd4 = "TEST111!";
        assertEquals(false, validator.isValid(pwd4));
        String pwd5 = "Test1!";
        assertEquals(false, validator.isValid(pwd5));
        String pwd6 = "TestTestTestTestTestTest1!";
        assertEquals(false, validator.isValid(pwd6));
    }

}
