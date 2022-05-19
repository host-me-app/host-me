package ch.epfl.sweng.hostme.database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class Auth {

    private static boolean test = false;

    private Auth() {

    }

    public static Task<AuthResult> loginUserWithEmail(String email, String password) {
        return Auth.getExactInstance().signInWithEmailAndPassword(email, password);
    }

    public static Task<AuthResult> createUser(String email, String password) {
        return Auth.getExactInstance().createUserWithEmailAndPassword(email, password);
    }

    public static Task<Void> updateEmail(String email) {
        return Auth.getCurrentUser().updateEmail(email);
    }

    public static Task<Void> resetEmail(String email) {
        return Auth.getExactInstance().sendPasswordResetEmail(email);
    }

    public static String getUid() {
        return Auth.getExactInstance().getUid();
    }

    public static FirebaseUser getCurrentUser() {
        return Auth.getExactInstance().getCurrentUser();
    }

    public static void signOut() {
        Auth.getExactInstance().signOut();
    }

    private static FirebaseAuth getExactInstance() {
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
