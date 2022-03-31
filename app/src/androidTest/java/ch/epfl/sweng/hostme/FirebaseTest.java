package ch.epfl.sweng.hostme;

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

import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;

public class FirebaseTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        Database.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void checkCreateUser() throws Exception {
        String username = "testlogin@gmail.com";
        String pwd = "fakePassword1!";
        Task<AuthResult> task = Auth.createUser(username, pwd);
        Tasks.await(task, 5000, TimeUnit.MILLISECONDS);
        assertTrue(task.isSuccessful());
    }

    @Test
    public void checkLoginUser() throws Exception {
        String username = "testlogin@gmail.com";
        String pwd = "fakePassword1!";
        Task<AuthResult> task = Auth.loginUserWithEmail(username, pwd);
        Tasks.await(task, 5000, TimeUnit.MILLISECONDS);
        assertTrue(task.isSuccessful());
    }

}
