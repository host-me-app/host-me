package ch.epfl.sweng.hostme.utils;

import static org.junit.Assert.assertEquals;
import static ch.epfl.sweng.hostme.utils.Constants.ADDRESS;
import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.BATH;
import static ch.epfl.sweng.hostme.utils.Constants.BEDS;
import static ch.epfl.sweng.hostme.utils.Constants.CITY;
import static ch.epfl.sweng.hostme.utils.Constants.DEPOSIT;
import static ch.epfl.sweng.hostme.utils.Constants.DURATION;
import static ch.epfl.sweng.hostme.utils.Constants.FURNISHED;
import static ch.epfl.sweng.hostme.utils.Constants.IMAGE_PATH;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_CHAT;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_CONVERSATIONS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_USERS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_EMAIL;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FCM_TOKEN;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FIRSTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_IMAGE;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_LASTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_LAST_MESSAGE;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_MESSAGE;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_RECEIVER_ID;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_RECEIVER_NAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_SENDER_ID;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_SENDER_NAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_TIMESTAMP;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_USER_ID;
import static ch.epfl.sweng.hostme.utils.Constants.KITCHEN;
import static ch.epfl.sweng.hostme.utils.Constants.LAUNDRY;
import static ch.epfl.sweng.hostme.utils.Constants.LEASE;
import static ch.epfl.sweng.hostme.utils.Constants.NAME;
import static ch.epfl.sweng.hostme.utils.Constants.NPA;
import static ch.epfl.sweng.hostme.utils.Constants.PETS;
import static ch.epfl.sweng.hostme.utils.Constants.PREVIEW_1_JPG;
import static ch.epfl.sweng.hostme.utils.Constants.PROPRIETOR;
import static ch.epfl.sweng.hostme.utils.Constants.RENT;
import static ch.epfl.sweng.hostme.utils.Constants.ROOM;
import static ch.epfl.sweng.hostme.utils.Constants.UID;
import static ch.epfl.sweng.hostme.utils.Constants.UTILITIES;

import org.junit.Test;


public class ConstantsTest {

    Constants constants = new Constants();

    @Test
    public void checkStrings() {
        assertEquals("users", constants.KEY_COLLECTION_USERS);
        assertEquals("chat", constants.KEY_COLLECTION_CHAT);
        assertEquals("conversations", constants.KEY_COLLECTION_CONVERSATIONS);
        assertEquals("senderId", constants.KEY_SENDER_ID);
        assertEquals("receiverId", constants.KEY_RECEIVER_ID);
        assertEquals("message", constants.KEY_MESSAGE);
        assertEquals("senderName", constants.KEY_SENDER_NAME);
        assertEquals("receiverName", constants.KEY_RECEIVER_NAME);
        assertEquals("lastMessage", constants.KEY_LAST_MESSAGE);
        assertEquals("timestamp", constants.KEY_TIMESTAMP);
        assertEquals("firstName", constants.KEY_FIRSTNAME);
        assertEquals("lastName", constants.KEY_LASTNAME);
        assertEquals("email", constants.KEY_EMAIL);
        assertEquals("userID", constants.KEY_USER_ID);
        assertEquals("image", constants.KEY_IMAGE);
        assertEquals("fcmToken", constants.KEY_FCM_TOKEN);
        assertEquals("uid", constants.UID);
        assertEquals("address", constants.ADDRESS);
        assertEquals("rent", constants.RENT);
        assertEquals("area", constants.AREA);
        assertEquals("currentLease", constants.LEASE);
        assertEquals("npa", constants.NPA);
        assertEquals("city", constants.CITY);
        assertEquals("beds", constants.BEDS);
        assertEquals("proprietor", constants.PROPRIETOR);
        assertEquals("name", constants.NAME);
        assertEquals("room", constants.ROOM);
        assertEquals("furnished", constants.FURNISHED);
        assertEquals("pets", constants.PETS);
        assertEquals("bath", constants.BATH);
        assertEquals("kitchen", constants.KITCHEN);
        assertEquals("laundry", constants.LAUNDRY);
        assertEquals("utilities", constants.UTILITIES);
        assertEquals("deposit", constants.DEPOSIT);
        assertEquals("duration", constants.DURATION);
        assertEquals("imagePath", constants.IMAGE_PATH);
        assertEquals("/preview1.jpg", constants.PREVIEW_1_JPG);
        assertEquals("apartments", constants.APARTMENTS);
    }
}
