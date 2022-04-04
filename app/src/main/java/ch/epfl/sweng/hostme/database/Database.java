package ch.epfl.sweng.hostme.database;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public final class Database {

    private Database() {

    }

    private static boolean test = false;

    public static CollectionReference getCollection(String collectionPath) {
        return getExactInstance().collection(collectionPath);
    }

    private static FirebaseFirestore getExactInstance() {
        if (test) {
            FirebaseFirestore fb = FirebaseFirestore.getInstance();
            try {
                fb.useEmulator("10.0.2.2", 8080);
            } catch (Exception ignored) {
            }
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false)
                    .build();
            fb.setFirestoreSettings(settings);
            return fb;
        } else {
            return FirebaseFirestore.getInstance();
        }
    }

    public static void setTest() {
        test = true;
    }

}
