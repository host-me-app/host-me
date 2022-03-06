package ch.epfl.sweng.hostme;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class EmailValidatorTest {

    @Test
    public void checkPatternIsCorrect() throws ExecutionException, InterruptedException, TimeoutException {
        EmailValidator email = new EmailValidator("test@gmail.com");
        assertEquals(email.checkValidity(), true);
    }

    @Test
    public void checkPatternIsIncorrect() throws ExecutionException, InterruptedException, TimeoutException {
        EmailValidator email = new EmailValidator("test@gmail.com.");
        assertEquals(email.checkValidity(), false);
        EmailValidator email2 = new EmailValidator(".test@gmail.com");
        assertEquals(email2.checkValidity(), false);
        EmailValidator email3 = new EmailValidator("testgmail.com");
        assertEquals(email3.checkValidity(), false);
        EmailValidator email4 = new EmailValidator("çtêst@gmail.com");
        assertEquals(email4.checkValidity(), false);
    }

    @Test
    public void checkUniquenessIsIncorrect() throws ExecutionException, InterruptedException, TimeoutException {
        EmailValidator email = new EmailValidator("host.me.app2022@gmail.com");
        assertEquals(email.checkValidity(), false);
    }

    @Test
    public void getEmailIsSame() {
        String emailString = "test@gmail.com";
        EmailValidator email = new EmailValidator("test@gmail.com");
        assertEquals(email.getEmail(), emailString);
    }
}
