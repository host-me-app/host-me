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

    final private String email;
    private static final FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private static final String REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public EmailValidator(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    private Boolean checkPattern() {
        return Pattern.compile(REGEX_PATTERN)
                .matcher(this.getEmail())
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

    private Boolean checkUniqueness() throws ExecutionException, InterruptedException, TimeoutException {
        return !getEmailFromDataBase().contains(this.getEmail());
    }

    public Boolean checkValidity() throws ExecutionException, InterruptedException, TimeoutException {
        return this.checkPattern() && this.checkUniqueness();
    }
}
