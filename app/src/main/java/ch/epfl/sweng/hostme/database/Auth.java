package ch.epfl.sweng.hostme.database;

import com.google.firebase.auth.FirebaseAuth;

public class Auth {

    public static com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> loginUserWithEmail(String email, String password) {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password);
    }

    public static com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> createUser(String email, String password) {
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password);
    }

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

}
