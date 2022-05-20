package ch.epfl.sweng.hostme.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.firebase.Timestamp;

import org.junit.Test;

public class ApartmentTest {

    @Test
    public void testApartment()  {
        Timestamp time = new Timestamp(0,0);
        Apartment apart = new Apartment(null, false, "a", "b", 1, "c", 2, false, new Timestamp(3, 4), "d", 5, false, "e", "f", "g", "h", "i",  6, false, "k", 7, "l", 8, "m");
        apart.setAddress("address");
        apart.setAvailable(true);
        apart.setCurrentLease(time);
        apart.setBath("bath");
        apart.setDeposit(1);
        apart.setFurnished(true);
        apart.setImagePath("image");
        apart.setKitchen("kitchen");
        apart.setLaundry("laundry");
        apart.setLid("lid");
        apart.setName("name");
        apart.setBeds(1);
        apart.setPets(true);
        apart.setProprietor("proprietor");
        apart.setRent(1);
        apart.setRoom("room");
        apart.setUtilities(1);
        apart.setArea(1);
        apart.setCity("city");
        apart.setNpa(1);
        apart.setUid("uid");
        apart.setDocID("id");
        apart.setFavorite(true);

        assertEquals("address", apart.getAddress());
        assertTrue(apart.isAvailable());
        assertEquals(time, apart.getCurrentLease());
        assertEquals("bath", apart.getBath());
        assertEquals(1, apart.getDeposit());
        assertTrue(apart.isFurnished());
        assertEquals("image", apart.getImagePath());
        assertEquals("kitchen", apart.getKitchen());
        assertEquals("laundry", apart.getLaundry());
        assertEquals("lid", apart.getLid());
        assertEquals("name", apart.getName());
        assertEquals(1, apart.getBeds());
        assertTrue(apart.isPets());
        assertEquals("proprietor", apart.getProprietor());
        assertEquals(1, apart.getRent());
        assertEquals("room", apart.getRoom());
        assertEquals(1, apart.getUtilities());
        assertEquals(1, apart.getArea());
        assertEquals("city", apart.getCity());
        assertEquals(1, apart.getNpa());
        assertEquals("uid", apart.getUid());
        assertEquals("id", apart.getDocID());
        assertEquals("uid", apart.getUid());
        assertTrue(apart.isFavorite());
    }
}
