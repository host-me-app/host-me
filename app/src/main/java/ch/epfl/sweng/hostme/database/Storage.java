package ch.epfl.sweng.hostme.database;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public final class Storage {

    private static boolean test = false;

    public static StorageReference getStorageReferenceByChild(String pathString) {
        return getAdaptedInstance().getReference().child(pathString);
    }

    private static FirebaseStorage getAdaptedInstance() {
        if (test) {
            FirebaseStorage fb = FirebaseStorage.getInstance();
            fb.useEmulator("10.0.2.2", 9199);
            return fb;
        } else {
            return FirebaseStorage.getInstance();
        }
    }

    public static void setTest() {
        test = true;
    }

}
