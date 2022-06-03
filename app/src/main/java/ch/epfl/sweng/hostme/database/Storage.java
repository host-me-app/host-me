package ch.epfl.sweng.hostme.database;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public final class Storage {

    private static boolean test = false;

    private Storage() {

    }

    /**
     * get the stoage reference
     * @param pathString to get the data
     * @return instance of storage
     */
    public static StorageReference getStorageReferenceByChild(String pathString) {
        return getExactInstance().getReference(pathString);
    }

    /**
     * get the exact instance
     * @return instance of firebase storage
     */
    private static FirebaseStorage getExactInstance() {
        if (test) {
            FirebaseStorage fb = FirebaseStorage.getInstance();
            fb.useEmulator("10.0.2.2", 9199);
            return fb;
        } else {
            return FirebaseStorage.getInstance();
        }
    }

    /**
     * set test mode activated
     */
    public static void setTest() {
        test = true;
    }

}
