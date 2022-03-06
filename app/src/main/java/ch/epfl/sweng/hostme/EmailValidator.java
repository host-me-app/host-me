package ch.epfl.sweng.hostme;

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

    private static Boolean checkPattern(String email) {
        return PATTERN.matcher(email)
                .matches();
    }

    private static List<String> getEmailFromDataBase() throws ExecutionException, InterruptedException, TimeoutException {
        List<String> emails = new ArrayList<>();
        Task<QuerySnapshot> task = DB.collection("users").get();
        Tasks.await(task, 1000, TimeUnit.MILLISECONDS);
        for (QueryDocumentSnapshot document : task.getResult()) {
            emails.add(document.getString("email"));
        }
        Log.i("DB", emails.toString());
        return emails;
    }

    private static Boolean checkUniqueness(String email) throws ExecutionException, InterruptedException, TimeoutException {
        return !getEmailFromDataBase().contains(email);
    }

    public static Boolean isValid(String email) throws ExecutionException, InterruptedException, TimeoutException {
        return checkPattern(email) && checkUniqueness(email);
    }
}
