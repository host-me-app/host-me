package ch.epfl.sweng.hostme.ui.search;

import static ch.epfl.sweng.hostme.utils.Constants.ADDR;
import static ch.epfl.sweng.hostme.utils.Constants.APART_ID;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.BITMAP;
import static ch.epfl.sweng.hostme.utils.Constants.CITY;
import static ch.epfl.sweng.hostme.utils.Constants.IMAGE_PATH;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_USERS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_EMAIL;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FCM_TOKEN;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FIRSTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_LASTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.LEASE;
import static ch.epfl.sweng.hostme.utils.Constants.NPA;
import static ch.epfl.sweng.hostme.utils.Constants.PROPRIETOR;
import static ch.epfl.sweng.hostme.utils.Constants.RENT;
import static ch.epfl.sweng.hostme.utils.Constants.UID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.maps.MapsFragment;
import ch.epfl.sweng.hostme.maps.StreetViewFragment;
import ch.epfl.sweng.hostme.ui.IOnBackPressed;
import ch.epfl.sweng.hostme.ui.messages.ChatActivity;
import ch.epfl.sweng.hostme.users.User;
import ch.epfl.sweng.hostme.utils.Constants;

public class DisplayApartment extends Fragment implements IOnBackPressed {

    private final CollectionReference reference = Database.getCollection(KEY_COLLECTION_USERS);
    private View root;
    private BottomNavigationView bottomNav;
    private String fullAddress;
    private String apartID;

    public DisplayApartment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.display_apartment, container, false);

        Button grade_button = root.findViewById(R.id.grade_button);
        grade_button.setOnClickListener(this::goToGradeFragment);
        Button maps_button = root.findViewById(R.id.maps_button);
        maps_button.setOnClickListener(this::goToMapsFragment);
        Button street_view_button = root.findViewById(R.id.street_view_button);
        street_view_button.setOnClickListener(this::goToStreetViewFragment);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            bottomNav = getActivity().findViewById(R.id.nav_view);
            bottomNav.setVisibility(View.GONE);
            apartID = bundle.getString(APART_ID);
            String addr = bundle.getString(ADDR);
            int area = bundle.getInt(AREA, 0);
            int rent = bundle.getInt(RENT, 0);
            String lease = bundle.getString(LEASE);
            String proprietor = bundle.getString(PROPRIETOR);
            String city = bundle.getString(CITY);
            int npa = bundle.getInt(NPA, 0);
            fullAddress = addr + " " + city + " " + npa;

            setHorizontalScrollable(bundle);

            changeText(String.valueOf(npa), R.id.npa);
            changeText(city, R.id.city);
            changeText(addr, R.id.addr);
            changeText(String.valueOf(area), R.id.area);
            changeText(String.valueOf(rent), R.id.price);
            changeText(lease, R.id.lease);
            changeText(proprietor, R.id.proprietor);

            String uid = bundle.getString(UID);
            Button contactUser = root.findViewById(R.id.contact_user_button);
            contactUser.setOnClickListener(view -> chatWithUser(uid));
        }
        return root;
    }

    /**
     * Set the horizontal scrollable view to have a list of images
     * @param bundle
     */
    private void setHorizontalScrollable(Bundle bundle) {
        LinearLayout gallery = root.findViewById(R.id.gallery);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.apart_image, gallery, false);
        ImageView image = view.findViewById(R.id.imageApart);
        Bitmap bitmap = bundle.getParcelable(BITMAP);
        image.setImageBitmap(bitmap);
        gallery.addView(view);
        String imagePath = bundle.getString(IMAGE_PATH);
        StorageReference storageReference = Storage.getStorageReferenceByChild(imagePath);
        final boolean[] first = {true};
        storageReference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference ref : listResult.getItems()) {
                if (first[0]) {
                    first[0] = false;
                    continue;
                }
                final File localFile;
                try {
                    localFile = File.createTempFile("preview1", "jpg");
                    View newView = layoutInflater.inflate(R.layout.apart_image, gallery, false);
                    ProgressBar loading = newView.findViewById(R.id.loading);
                    loading.setVisibility(View.VISIBLE);
                    ref.getFile(localFile)
                            .addOnSuccessListener(result -> {
                                Bitmap newBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                ImageView imageView = newView.findViewById(R.id.imageApart);
                                imageView.setImageBitmap(newBitmap);
                                gallery.addView(newView);
                                loading.setVisibility(View.GONE);
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void goToStreetViewFragment(View view) {
        Bundle bundle = new Bundle();
        Fragment fragment = new StreetViewFragment();
        FragmentTransaction fragmentTransaction =
                ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        bundle.putString("address", this.fullAddress);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

    private void goToMapsFragment(View view) {
        Bundle bundle = new Bundle();
        Fragment fragment = new MapsFragment();
        FragmentTransaction fragmentTransaction =
                ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        bundle.putString("address", this.fullAddress);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

    private void goToGradeFragment(View view) {
        Bundle bundle = new Bundle();
        Fragment fragment = new GradeApartment();
        FragmentTransaction fragmentTransaction =
                ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        bundle.putString(APART_ID, this.apartID);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }


    /**
     * launch the activity to chat with the owner of the apartment
     *
     * @param uid
     */
    private void chatWithUser(String uid) {
        reference.get().addOnSuccessListener(result -> {
            QuerySnapshot snapshot = result;
            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                if (doc.getId().equals(uid)) {
                    User user = new User(doc.getString(KEY_FIRSTNAME) + " " +
                            doc.getString(KEY_LASTNAME),
                            null, doc.getString(KEY_EMAIL), doc.getString(KEY_FCM_TOKEN), uid);
                    Intent newIntent = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
                    newIntent.putExtra(Constants.FROM, apartID);
                    newIntent.putExtra(Constants.KEY_USER, user);
                    startActivity(newIntent);
                    getActivity().finish();
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

    @Override
    public boolean onBackPressed() {
        bottomNav.setVisibility(View.VISIBLE);
        return false;
    }
}
