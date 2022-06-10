package ch.epfl.sweng.hostme.database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class Auth {

    private static boolean test = false;

    private Auth() {

    }

    /**
     * login the user with his email
     *
     * @param email    of the user
     * @param password of the user
     * @return task of user sign in
     */
    public static Task<AuthResult> loginUserWithEmail(String email, String password) {
        return Auth.getExactInstance().signInWithEmailAndPassword(email, password);
    }

    /**
     * creat user with his email and password
     *
     * @param email    of the user
     * @param password of the user
     * @return task of the creation
     */
    public static Task<AuthResult> createUser(String email, String password) {
        return Auth.getExactInstance().createUserWithEmailAndPassword(email, password);
    }

    /**
     * update the email
     *
     * @param email of the user
     * @return task of the email update
     */
    public static Task<Void> updateEmail(String email) {
        return Auth.getCurrentUser().updateEmail(email);
    }

    /**
     * reset the user email
     *
     * @param email of the user
     * @return task of the reset password with an email
     */
    public static Task<Void> resetEmail(String email) {
        return Auth.getExactInstance().sendPasswordResetEmail(email);
    }

    /**
     * get the user id
     *
     * @return the user id
     */
    public static String getUid() {
        return Auth.getExactInstance().getUid();
    }

    /**
     * get the current user
     *
     * @return the current user
     */
    public static FirebaseUser getCurrentUser() {
        return Auth.getExactInstance().getCurrentUser();
    }

    /**
     * sign out
     */
    public static void signOut() {
        Auth.getExactInstance().signOut();
    }

    /**
     * get the instance of Firebase for testing or not
     *
     * @return the instance of FirebaseAuth
     */
    private static FirebaseAuth getExactInstance() {
        if (test) {
            FirebaseAuth fb = FirebaseAuth.getInstance();
            fb.useEmulator("10.0.2.2", 9099);
            return fb;
        } else {
            return FirebaseAuth.getInstance();
        }
    }

    /**
     * set the test mode activated
     */
    public static void setTest() {
        test = true;
    }

}
