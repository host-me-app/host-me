package ch.epfl.sweng.hostme.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PrivacyTest {

    @Test
    public void testEnum() {
        assertEquals("None", Privacy.NONE.toString());
    }
}
