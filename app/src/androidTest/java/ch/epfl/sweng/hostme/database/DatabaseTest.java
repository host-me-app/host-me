package ch.epfl.sweng.hostme.database;

import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DatabaseTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void checkUserInFirestore() throws Exception {
        List<String> emails = new ArrayList<>();
        Task<QuerySnapshot> task = Database.getCollection("users").get();
        Tasks.await(task, 5000, TimeUnit.MILLISECONDS);
        for (QueryDocumentSnapshot document : task.getResult()) {
            emails.add(document.getString("email"));
        }
        assertTrue(emails.contains("testlogin@gmail.com"));
    }
}
