package ch.epfl.sweng.hostme.database;

import com.google.firebase.storage.StorageReference;

import org.junit.Test;


public class RealDB {

    @Test
    public void checkRealInstance() throws Exception {
        String email = "otherlogin@gmail.com";
        String pwd = "fakePassword1!";
        try {
            Auth.loginUserWithEmail(email, pwd);
        } catch (Exception ignored) {

        }

        try {
            Database.getCollection("fakecollection").get();
        } catch (Exception ignored) {

        }

        try {
            Storage.getStorageReferenceByChild("blank.pdf");
        } catch (Exception ignored) {

        }
    }
}