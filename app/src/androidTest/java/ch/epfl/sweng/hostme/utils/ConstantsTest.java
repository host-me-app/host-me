package ch.epfl.sweng.hostme.utils;

import static org.junit.Assert.assertEquals;

import static ch.epfl.sweng.hostme.utils.Constants.ADDR;
import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;
import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS_PATH;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.CITY;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_USERS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_EMAIL;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FCM_TOKEN;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FIRSTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_IMAGE;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_LASTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_USER_ID;
import static ch.epfl.sweng.hostme.utils.Constants.LEASE;
import static ch.epfl.sweng.hostme.utils.Constants.LID;
import static ch.epfl.sweng.hostme.utils.Constants.NPA;
import static ch.epfl.sweng.hostme.utils.Constants.OCCUPANT;
import static ch.epfl.sweng.hostme.utils.Constants.PREVIEW_1_JPG;
import static ch.epfl.sweng.hostme.utils.Constants.PROPRIETOR;
import static ch.epfl.sweng.hostme.utils.Constants.RENT;
import static ch.epfl.sweng.hostme.utils.Constants.UID;

import org.junit.Test;


public class ConstantsTest {

    @Test
    public void checkStrings() {
        assertEquals("users", KEY_COLLECTION_USERS);
        assertEquals("firstName", KEY_FIRSTNAME);
        assertEquals("lastName", KEY_LASTNAME);
        assertEquals("Mail", KEY_EMAIL);
        assertEquals("userID", KEY_USER_ID);
        assertEquals("image", KEY_IMAGE);
        assertEquals("fcmToken", KEY_FCM_TOKEN);
        assertEquals("uid", UID);
        assertEquals("addr", ADDR);
        assertEquals("rent", RENT);
        assertEquals("area", AREA);
        assertEquals("lid", LID);
        assertEquals("lease", LEASE);
        assertEquals("npa", NPA);
        assertEquals("city", CITY);
        assertEquals("occupant", OCCUPANT);
        assertEquals("propietor", PROPRIETOR);
        assertEquals("apartments/", APARTMENTS_PATH);
        assertEquals("/preview1.jpg", PREVIEW_1_JPG);
        assertEquals("apartments", APARTMENTS);
    }
}
