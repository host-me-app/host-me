package ch.epfl.sweng.hostme.database;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public final class Database {

    private static boolean test = false;

    public static CollectionReference getCollection(String collectionPath) {
        return getAdaptedInstance().collection(collectionPath);
    }

    private static FirebaseFirestore getAdaptedInstance() {
        if (test) {
            FirebaseFirestore fb = FirebaseFirestore.getInstance();
            fb.useEmulator("10.0.2.2", 8080);
            return fb;
        } else {
            return FirebaseFirestore.getInstance();
        }
    }

    public static void setTest() {
        test = true;
    }

}
