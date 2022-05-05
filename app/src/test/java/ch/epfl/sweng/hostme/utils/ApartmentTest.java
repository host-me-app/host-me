package ch.epfl.sweng.hostme.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;

public class ApartmentTest {

    @Test
    public void checkGetterSetterApartment() {
        Apartment apartment = new Apartment();
        apartment.setAddress("Street");
        assertEquals("Street", apartment.getAddress());
        apartment.setAvailable(false);
        assertFalse(apartment.isAvailable());
        apartment.setArea(120);
        assertEquals(120, apartment.getArea());
        apartment.setBath("bath");
        assertEquals("bath", apartment.getBath());
        apartment.setCity("Lausanne");
        assertEquals("Lausanne", apartment.getCity());
        apartment.setCurrentLease(new Timestamp(new Date(1645784100)));
        assertEquals(new Timestamp(new Date(1645784100)), apartment.getCurrentLease());
        apartment.setFurnished(true);
        assertTrue(apartment.isFurnished());
        apartment.setImagePath("/apartments/test");
        assertEquals("/apartments/test", apartment.getImagePath());
        apartment.setDeposit(1300);
        assertEquals(1300, apartment.getDeposit());
        apartment.setKitchen("kitchen");
        assertEquals("kitchen", apartment.getKitchen());
        apartment.setLaundry("laundry");
        assertEquals("laundry", apartment.getLaundry());
        apartment.setLid("lid");
        assertEquals("lid", apartment.getLid());
        apartment.setName("Rooom22");
        assertEquals("Rooom22", apartment.getName());
        apartment.setNpa(1024);
        assertEquals(1024, apartment.getNpa());
        apartment.setBeds(10);
        assertEquals(10, apartment.getBeds());
        apartment.setPets(false);
        assertFalse(apartment.isPets());
        apartment.setProprietor("FMEL");
        assertEquals("FMEL", apartment.getProprietor());
        apartment.setRent(700);
        assertEquals(700, apartment.getRent());
        apartment.setRoom("room1");
        assertEquals("room1", apartment.getRoom());
        apartment.setUid("abcdefg");
        assertEquals("abcdefg", apartment.getUid());
        apartment.setUtilities(4);
        assertEquals(4, apartment.getUtilities());
        Apartment apartment1 = new Apartment(false, null, apartment.getCity(),
                apartment.getNpa(), apartment.getAddress(),
                apartment.getArea(), apartment.isAvailable(), apartment.getCurrentLease(),
                apartment.getBath(), apartment.getDeposit(), apartment.isFurnished(),
                apartment.getImagePath(), apartment.getKitchen(), apartment.getLaundry(),
                apartment.getLid(), apartment.getName(), apartment.getBeds(),
                apartment.isPets(), apartment.getProprietor(), apartment.getRent(),
                apartment.getRoom(), apartment.getUtilities(), apartment.getUid());
    }

}
