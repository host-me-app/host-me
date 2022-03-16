package ch.epfl.sweng.hostme;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.sweng.hostme.utils.PasswordValidator;

public class PasswordValidatorTest {


    @Test
    public void checkPatternIsCorrect() {
        PasswordValidator validator = new PasswordValidator();
        String pwd = "Test111!";
        assertEquals(validator.isValid(pwd), true);
    }

    @Test
    public void checkPatternIsInCorrect() {
        PasswordValidator validator = new PasswordValidator();
        String pwd = "testTest!";
        assertEquals(validator.isValid(pwd), false);
        String pwd2 = "Test1111";
        assertEquals(validator.isValid(pwd2), false);
        String pwd3 = "test111!";
        assertEquals(validator.isValid(pwd3), false);
        String pwd4 = "TEST111!";
        assertEquals(validator.isValid(pwd4), false);
        String pwd5 = "Test1!";
        assertEquals(validator.isValid(pwd5), false);
        String pwd6 = "TestTestTestTestTestTest1!";
        assertEquals(validator.isValid(pwd6), false);
    }

}
