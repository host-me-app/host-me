package ch.epfl.sweng.hostme.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.firebase.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Date;

public class ListingTest {

    @Test
    public void testListing() throws JSONException {
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

        Listing lst = new Listing(fields);

        lst.setName("name");
        lst.setRoom("room");
        lst.setAddress("address");
        lst.setNpa(1024);
        lst.setCity("city");
        lst.setRent(600);
        lst.setBeds(1);
        lst.setArea(25);
        lst.toggleFurnished();
        lst.toggleFurnished();
        lst.setBath("bath");
        lst.setKitchen("kitchen");
        lst.setLaundry("laundry");
        lst.togglePets();
        lst.togglePets();
        lst.toggleAvailable();
        lst.toggleAvailable();
        lst.setProprietor("proprietor");
        lst.setUtilities(200);
        lst.setDeposit(2000);
        lst.setDuration(6);

        Timestamp tmp = new Timestamp(new Date(5));
        lst.setCurrentLease(tmp);
        assertEquals(tmp, lst.getCurrentLease());

        assertEquals("name", lst.getName());
        assertEquals("name", lst.getName());
        assertEquals("room", lst.getRoom());
        assertEquals("address", lst.getAddress());
        assertEquals("city", lst.getCity());
        assertEquals(1024, lst.getNpa());
        assertEquals(600, lst.getRent());
        assertEquals(1, lst.getBeds());
        assertEquals(25, lst.getArea());
        assertFalse(lst.isFurnished());
        assertEquals("bath", lst.getBath());
        assertEquals("kitchen", lst.getKitchen());
        assertEquals("laundry", lst.getLaundry());
        assertFalse(lst.isPets());
        assertTrue(lst.isAvailable());
        assertEquals("imagePath", lst.getImagePath());
        assertEquals("proprietor", lst.getProprietor());
        assertEquals("uid", lst.getUid());
        assertEquals(200, lst.getUtilities());
        assertEquals(2000, lst.getDeposit());
        assertEquals(6, lst.getDuration());

        JSONObject jlst = lst.exportDoc();
        assertEquals(jlst.toString(), lst.toString());
    }

}
