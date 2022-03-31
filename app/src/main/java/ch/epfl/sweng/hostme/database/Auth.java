package ch.epfl.sweng.hostme.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class Auth {

    private static boolean test = false;

    public static com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> loginUserWithEmail(String email, String password) {
        return getExactInstance().signInWithEmailAndPassword(email, password);
    }

    public static com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> createUser(String email, String password) {
        return getExactInstance().createUserWithEmailAndPassword(email, password);
    }

    public static com.google.android.gms.tasks.Task<Void> resetEmail(String email) {
        return getExactInstance().sendPasswordResetEmail(email);
    }

    public static String getUid() {
        return getExactInstance().getUid();
    }

    public static FirebaseUser getCurrentUser() {
        return getExactInstance().getCurrentUser();
    }

    public static void signOut() {
        getExactInstance().signOut();
    }

    private static FirebaseAuth getExactInstance() {
        FirebaseAuth fb = FirebaseAuth.getInstance();
        fb.useEmulator("10.0.2.2", 9099);
        return fb;
    }

    public static void setTest() {
        test = true;
    }

}
