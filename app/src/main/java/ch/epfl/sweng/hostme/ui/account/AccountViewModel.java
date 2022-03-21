package ch.epfl.sweng.hostme.ui.account;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sweng.hostme.Profile;


interface ProfileRetreiver{

    Profile getProfile();

}
public class AccountViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
//    private FirebaseUser user;
//    private FirebaseAuth mAuth;
//    private ProfileRetreiver profRet;
//    private final static FirebaseFirestore database = FirebaseFirestore.getInstance();

    public AccountViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is account fragment");

//        mAuth = FirebaseAuth.getInstance();
//        this.profRet = new ProfileRetreiver() {
//            @Override
//            public Profile getProfile() {
//
//                Profile userProfile;
//                DocumentReference docRef = database.collection("users")
//                        .document(mAuth.getUid());
//                Task<DocumentSnapshot> doc = docRef.get().addOnCompleteListener(
//                        task -> {
//                            if (task.isSuccessful()) {
//                                updateFireStoreDB();
//                                Toast.makeText(getActivity(), "Authentication successed.",
//                                        Toast.LENGTH_SHORT).show();
//                                welcome();
//                            } else {
//                                Toast.makeText(getActivity(), "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                );
//                return doc.getResult().toObject(Profile.class);
//            }
//        };


    }



    public LiveData<String> getText() {
        return mText;
    }

//    public Profile getfullProfile(){
//        return profRet.getProfile();
//    }


}