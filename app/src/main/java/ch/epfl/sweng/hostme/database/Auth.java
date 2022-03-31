package ch.epfl.sweng.hostme.database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class Auth {

    private static boolean test = false;

    public static Task<AuthResult> loginUserWithEmail(String email, String password) {
        return getExactInstance().signInWithEmailAndPassword(email, password);
    }

    public static Task<AuthResult> createUser(String email, String password) {
        return getExactInstance().createUserWithEmailAndPassword(email, password);
    }

    public static Task<Void> resetEmail(String email) {
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
        if (test) {
            FirebaseAuth fb = FirebaseAuth.getInstance();
            fb.useEmulator("localhost", 9099);
            return fb;
        } else {
            return FirebaseAuth.getInstance();
        }
    }

    public static void setTest() {
        test = true;
    }

}
