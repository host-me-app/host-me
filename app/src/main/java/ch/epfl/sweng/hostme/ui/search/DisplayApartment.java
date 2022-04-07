package ch.epfl.sweng.hostme.ui.search;

import static ch.epfl.sweng.hostme.utils.Constants.ADDR;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.CITY;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_USERS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_EMAIL;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FIRSTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_LASTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.LEASE;
import static ch.epfl.sweng.hostme.utils.Constants.NPA;
import static ch.epfl.sweng.hostme.utils.Constants.PROPRIETOR;
import static ch.epfl.sweng.hostme.utils.Constants.RENT;
import static ch.epfl.sweng.hostme.utils.Constants.UID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.ui.messages.ChatActivity;
import ch.epfl.sweng.hostme.users.User;
import ch.epfl.sweng.hostme.utils.Constants;

public class DisplayApartment extends Fragment {

    private final CollectionReference reference = Database.getCollection(KEY_COLLECTION_USERS);
    private View root;
    public static final String LID = "lid";

    public DisplayApartment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.display_apartment, container, false);
        BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.GONE);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String lid = bundle.getString(LID);
            String addr = bundle.getString(ADDR);
            int area = bundle.getInt(AREA, 0);
            int rent = bundle.getInt(RENT, 0);
            String lease = bundle.getString(LEASE);
            String proprietor = bundle.getString(PROPRIETOR);
            String city = bundle.getString(CITY);
            int npa = bundle.getInt(NPA, 0);
            ImageView image = root.findViewById(R.id.apart_image);
            Bitmap bitmap = bundle.getParcelable(ApartmentAdapter.BITMAP);
            image.setImageBitmap(bitmap);

            changeText(String.valueOf(npa), R.id.npa);
            changeText(city, R.id.city);
            changeText(addr, R.id.addr);
            changeText(String.valueOf(area), R.id.area);
            changeText(String.valueOf(rent), R.id.price);
            changeText(lease, R.id.lease);
            changeText(proprietor, R.id.proprietor);

            String uid = bundle.getString(UID);
            Button contactUser = root.findViewById(R.id.contact_user_button);
            contactUser.setOnClickListener(view -> {
                chatWithUser(uid);
            });
        }

        return root;
    }

    /**
     * launch the activity to chat with the owner of the apartment
     *
     * @param uid
     */
    private void chatWithUser(String uid) {
        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                for (DocumentSnapshot doc : snapshot.getDocuments()) {
                    if (doc.getId().equals(uid)) {
                        User user = new User(doc.getString(KEY_FIRSTNAME) + " " +
                                doc.getString(KEY_LASTNAME),
                                null, doc.getString(KEY_EMAIL), null, null);
                        Intent newIntent = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
                        newIntent.putExtra(Constants.KEY_USER, user);
                        startActivity(newIntent);
                        getActivity().finish();
                    }
                }
            }
        });
    }


    /**
     * change the text view to display the data
     *
     * @param addr
     * @param id
     */
    private void changeText(String addr, int id) {
        TextView addrText = root.findViewById(id);
        addrText.setText(addr);
    }
}
