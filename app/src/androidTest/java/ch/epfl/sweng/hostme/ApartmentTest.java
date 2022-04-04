package ch.epfl.sweng.hostme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.FirebaseApp;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.utils.Apartment;


public class ApartmentTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

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
        apartment.setCurrentLease(new Date(2020));
        assertEquals(new Date(2020).getTime(), apartment.getCurrentLease().getTime());
        apartment.setFurnished(true);
        assertTrue(apartment.isFurnished());
        apartment.setImage_path("/apartments/test");
        assertEquals("/apartments/test", apartment.getImage_path());
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
        apartment.setOccupants(10);
        assertEquals(10, apartment.getOccupants());
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
        Apartment apartment1 = new Apartment(apartment.getCity(), apartment.getNpa(), apartment.getAddress(),
                apartment.getArea(), apartment.isAvailable(), apartment.getCurrentLease(),
                apartment.getBath(), apartment.getDeposit(), apartment.isFurnished(),
                apartment.getImage_path(), apartment.getKitchen(), apartment.getLaundry(),
                apartment.getLid(), apartment.getName(), apartment.getOccupants(),
                apartment.isPets(), apartment.getProprietor(), apartment.getRent(),
                apartment.getRoom(), apartment.getUtilities(), apartment.getUid());
    }

}
