package ch.epfl.sweng.hostme.utils;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

public class EmailValidator {

    private static final FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private static final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]"
            + "{2,})$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

    /**
     * Check if the email has a valid pattern
     *
     * @param email
     * @return True if the email has a valid pattern
     */
    public static Boolean checkPattern(String email) {
        return PATTERN.matcher(email)
                .matches();
    }

    /**
     * Fetch emails from the database
     *
     * @return Emails from accounts in the database
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    private static List<String> getEmailFromDataBase() throws ExecutionException, InterruptedException, TimeoutException {
        List<String> emails = new ArrayList<>();
        Task<QuerySnapshot> task = DB.collection("users").get();
        Tasks.await(task, 5000, TimeUnit.MILLISECONDS);
        for (QueryDocumentSnapshot document : task.getResult()) {
            emails.add(document.getString("email"));
        }
        Log.i("DB", emails.toString());
        return emails;
    }

    /**
     * Check if is the email is already used
     *
     * @param email
     * @return True if the email is not used in the app
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    private static Boolean checkUniqueness(String email) throws ExecutionException, InterruptedException, TimeoutException {
        return !getEmailFromDataBase().contains(email);
    }

    /**
     * Check email validity (uniqueness and pattern)
     *
     * @param email
     * @return True if email is valid
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public static Boolean isValid(String email) throws ExecutionException, InterruptedException, TimeoutException {
        return checkPattern(email) && checkUniqueness(email);
    }
}
