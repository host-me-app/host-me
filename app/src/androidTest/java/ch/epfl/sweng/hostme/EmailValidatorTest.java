package ch.epfl.sweng.hostme;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import ch.epfl.sweng.hostme.utils.EmailValidator;

public class EmailValidatorTest {

    EmailValidator validator = new EmailValidator();

    @Test
    public void checkEmailIsCorrect() throws ExecutionException, InterruptedException, TimeoutException {
        String email = "test@gmail.com";
        assertEquals(validator.isValid(email), true);
    }

    @Test
    public void checkPatternIsIncorrect() throws ExecutionException, InterruptedException, TimeoutException {
        String email = "test@gmail.com.";
        assertEquals(validator.isValid(email), false);
        String email2 = ".test@gmail.com";
        assertEquals(validator.isValid(email2), false);
        String email3 = "testgmail.com";
        assertEquals(validator.isValid(email3), false);
        String email4 = "çtêst@gmail.com";
        assertEquals(validator.isValid(email4), false);
    }

    @Test
    public void checkUniquenessIsIncorrect() throws ExecutionException, InterruptedException, TimeoutException {
        String email = "host.me.app2022@gmail.com";
        assertEquals(validator.isValid(email), false);
    }
}
