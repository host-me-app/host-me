package ch.epfl.sweng.hostme.database;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public final class Storage {

    private static boolean test = false;

    public static StorageReference getStorageReferenceByChild(String pathString) {
        return getExactInstance().getReference().child(pathString);
    }

    private static FirebaseStorage getExactInstance() {
        if (test) {
            FirebaseStorage fb = FirebaseStorage.getInstance();
            try {
                fb.useEmulator("10.0.2.2", 9199);
            } catch (Exception ignored) {
            }
            return fb;
        } else {
            return FirebaseStorage.getInstance();
        }
    }

    public static void setTest() {
        test = true;
    }

}
