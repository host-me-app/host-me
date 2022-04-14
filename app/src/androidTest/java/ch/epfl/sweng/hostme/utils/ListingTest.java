package ch.epfl.sweng.hostme.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;

import com.google.firebase.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
        fields.put("bath", "none");
        fields.put("kitchen", "none");
        fields.put("laundry", "none");
        fields.put("pets", false);
        fields.put("proprietor", "");
        fields.put("uid", "uid");
        fields.put("utilities", 0);
        fields.put("deposit", 0);
        fields.put("duration", 0);

        Listing lst = new Listing(fields);
        lst.exportDoc();
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
        lst.setBath(Privacy.PRIVATE);
        lst.setKitchen(Privacy.SHARED);
        lst.setLaundry(Privacy.SHARED);
        lst.togglePets();
        lst.togglePets();
        lst.toggleAvailable();
        lst.toggleAvailable();
        lst.setProprietor("propietor");
        lst.setUtilities(2);
        lst.setDeposit(2000);
        lst.setDuration(6);
        Timestamp tmp = new Timestamp(new Date(5));
        lst.setCurrentLease(tmp);

        assertEquals(tmp, lst.getCurrentLease());
        List<Bitmap> img = lst.getImages();
        Map<String, String> opt = lst.getOpt();
        String imgPath = lst.getImagePath();

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
        assertEquals("Private", lst.getBath());
        assertEquals("Shared", lst.getKitchen());
        assertEquals("Shared", lst.getLaundry());
        assertFalse(lst.isPets());
        assertTrue(lst.isAvailable());
        assertEquals("uid", lst.getUid());
        assertEquals("propietor", lst.getProprietor());
        assertEquals(2, lst.getUtilities());
        assertEquals(2000, lst.getDeposit());
        assertEquals(6, lst.getDuration());
    }

}
