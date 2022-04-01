package ch.epfl.sweng.hostme.database;

import static org.junit.Assert.assertNotNull;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.StorageReference;

import org.junit.BeforeClass;
import org.junit.Test;

public class StorageTest {

    @BeforeClass
    public static void setUp() {
        Storage.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void checkBlankReferenceIsNotNull() {
        StorageReference ref = Storage.getStorageReferenceByChild("blank.pdf");
        assertNotNull(ref);
    }
}
