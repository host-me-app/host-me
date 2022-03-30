package ch.epfl.sweng.hostme.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Auth {

    private static boolean test = false;

    public static com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> loginUserWithEmail(String email, String password) {
        return getAdaptedInstance().signInWithEmailAndPassword(email, password);
    }

    public static com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> createUser(String email, String password) {
        return getAdaptedInstance().createUserWithEmailAndPassword(email, password);
    }

    public static FirebaseAuth getAuth() {
        return getAdaptedInstance();
    }

    /**
     * @return a firebase database that may use a local emulator or not,
     * depending on state.
     */
    public static FirebaseAuth getAdaptedInstance() {
        FirebaseAuth fb = FirebaseAuth.getInstance();
        fb.useEmulator("10.0.2.2", 9099);
        return fb;
    }

    public static void setTest() {
        test = true;
    }

}
