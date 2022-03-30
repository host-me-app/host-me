package ch.epfl.sweng.hostme.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class Auth {

    private static boolean test = false;

    public static com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> loginUserWithEmail(String email, String password) {
        return getAdaptedInstance().signInWithEmailAndPassword(email, password);
    }

    public static com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> createUser(String email, String password) {
        return getAdaptedInstance().createUserWithEmailAndPassword(email, password);
    }

    public static com.google.android.gms.tasks.Task<Void> resetEmail(String email) {
        return getAdaptedInstance().sendPasswordResetEmail(email);
    }

    public static String getUid() {
        return getAdaptedInstance().getUid();
    }

    public static FirebaseUser getCurrentUser() {
        return getAdaptedInstance().getCurrentUser();
    }

    public static void signOut() {
        getAdaptedInstance().signOut();
    }

    private static FirebaseAuth getAdaptedInstance() {
        if (test) {
            FirebaseAuth fb = FirebaseAuth.getInstance();
            fb.useEmulator("10.0.2.2", 9099);
            return fb;
        } else {
            return FirebaseAuth.getInstance();
        }
    }

    public static void setTest() {
        test = true;
    }

}
