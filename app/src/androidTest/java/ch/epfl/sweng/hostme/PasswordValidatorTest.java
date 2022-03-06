package ch.epfl.sweng.hostme;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PasswordValidatorTest {

    PasswordValidator validator = new PasswordValidator();

    @Test
    public void checkPatternIsCorrect() {
        String pwd = "Test111!";
        assertEquals(validator.isValid(pwd), true);
    }

    @Test
    public void checkPatternIsInCorrect() {
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
