package ch.epfl.sweng.hostme.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class AuthTest {

    @BeforeClass
    public static void setUp() {
        Auth.setTest();
        FirebaseApp.clearInstancesForTest();
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void checkLoginUser() throws Exception {
        String email = "testlogin@gmail.com";
        String pwd = "fakePassword1!";
        Task<AuthResult> task = Auth.loginUserWithEmail(email, pwd);
        Tasks.await(task, 1000, TimeUnit.MILLISECONDS);
        assertTrue(task.isSuccessful());
    }

    @Test
    public void checkUidIsCorrect() throws Exception {
        String email = "testlogin@gmail.com";
        String pwd = "fakePassword1!";
        String uid = "twLMR1WN7wCqQBmnChMvoSmx3jP9";
        Task<AuthResult> task = Auth.loginUserWithEmail(email, pwd);
        Tasks.await(task, 1000, TimeUnit.MILLISECONDS);
        assertTrue(task.isSuccessful());
        assertEquals(uid, Auth.getUid());
    }

    @Test
    public void checkCreateUserWorks() throws Exception {
        String email = "otherlogin@gmail.com";
        String pwd = "fakePassword1!";
        Task<AuthResult> task = Auth.createUser(email, pwd);
        Tasks.await(task, 1000, TimeUnit.MILLISECONDS);
        assertTrue(task.isSuccessful());
    }

    @Test
    public void checkUpdateEmailWorks() throws Exception {
        String email = "otherlogin@gmail.com";
        String newEmail = "otherlogin2@gmail.com";
        String pwd = "fakePassword1!";
        Task<AuthResult> task = Auth.createUser(email, pwd);
        Tasks.await(task, 1000, TimeUnit.MILLISECONDS);
        assertTrue(task.isSuccessful());
        Task<AuthResult> task2 = Auth.loginUserWithEmail(email, pwd);
        Tasks.await(task2, 1000, TimeUnit.MILLISECONDS);
        assertTrue(task2.isSuccessful());
        Task<Void> task3 = Auth.updateEmail(newEmail);
        Tasks.await(task3, 1000, TimeUnit.MILLISECONDS);
        assertTrue(task3.isSuccessful());
    }

    @Test
    public void checkResetEmailWorks() throws Exception {
        String email = "testlogin@gmail.com";
        Task<Void> task = Auth.resetEmail(email);
        Tasks.await(task, 1000, TimeUnit.MILLISECONDS);
        assertTrue(task.isSuccessful());
    }

    @Test
    public void checkSignOutWorks() throws Exception {
        String email = "testlogin@gmail.com";
        String pwd = "fakePassword1!";
        Task<AuthResult> task = Auth.loginUserWithEmail(email, pwd);
        Tasks.await(task, 1000, TimeUnit.MILLISECONDS);
        assertTrue(task.isSuccessful());
        Auth.signOut();
        assertNull(Auth.getCurrentUser());
    }
}
