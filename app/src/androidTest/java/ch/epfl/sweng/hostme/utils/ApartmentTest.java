package ch.epfl.sweng.hostme.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.firebase.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Date;

public class ApartmentTest {

    @Test
    public void testApartment() throws JSONException {
        JSONObject fields = new JSONObject();
        fields.put("name", "");
        fields.put("room", "");
        fields.put("address", "");
        fields.put("npa", 0);
        fields.put("city", "");
        fields.put("rent", 0);
        fields.put("beds", 0);
        fields.put("area", 0);
        fields.put("furnished", false);
        fields.put("bath", "");
        fields.put("kitchen", "");
        fields.put("laundry", "");
        fields.put("pets", false);
        fields.put("imagePath", "imagePath");
        fields.put("proprietor", "");
        fields.put("uid", "uid");
        fields.put("utilities", 0);
        fields.put("deposit", 0);
        fields.put("duration", 0);

        Apartment apt = new Apartment(fields);

        apt.setName("name");
        apt.setRoom("room");
        apt.setAddress("address");
        apt.setNpa(1024);
        apt.setCity("city");
        apt.setRent(600);
        apt.setBeds(1);
        apt.setArea(25);
        apt.toggleFurnished();
        apt.toggleFurnished();
        apt.setBath("bath");
        apt.setKitchen("kitchen");
        apt.setLaundry("laundry");
        apt.togglePets();
        apt.togglePets();
        apt.toggleAvailable();
        apt.toggleAvailable();
        apt.setProprietor("proprietor");
        apt.setUtilities(200);
        apt.setDeposit(2000);
        apt.setDuration(6);
        apt.setDocId("testApt");

        Timestamp tmp = new Timestamp(new Date(5));
        apt.setCurrentLease(tmp);
        assertEquals(tmp, apt.getCurrentLease());

        assertEquals("name", apt.getName());
        assertEquals("name", apt.getName());
        assertEquals("room", apt.getRoom());
        assertEquals("address", apt.getAddress());
        assertEquals("city", apt.getCity());
        assertEquals(1024, apt.getNpa());
        assertEquals(600, apt.getRent());
        assertEquals(1, apt.getBeds());
        assertEquals(25, apt.getArea());
        assertFalse(apt.isFurnished());
        assertEquals("bath", apt.getBath());
        assertEquals("kitchen", apt.getKitchen());
        assertEquals("laundry", apt.getLaundry());
        assertFalse(apt.isPets());
        assertTrue(apt.isAvailable());
        assertEquals("imagePath", apt.getImagePath());
        assertEquals("proprietor", apt.getProprietor());
        assertEquals("uid", apt.getUid());
        assertEquals(200, apt.getUtilities());
        assertEquals(2000, apt.getDeposit());
        assertEquals(6, apt.getDuration());
        assertEquals("testApt", apt.getDocId());

        JSONObject japt = apt.exportDoc();
        assertEquals(japt.toString(), apt.toString());
    }

}