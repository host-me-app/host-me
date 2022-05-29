package ch.epfl.sweng.hostme.ui.search;

import static ch.epfl.sweng.hostme.utils.Constants.ADDRESS;
import static ch.epfl.sweng.hostme.utils.Constants.APART_ID;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.BITMAP;
import static ch.epfl.sweng.hostme.utils.Constants.CITY;
import static ch.epfl.sweng.hostme.utils.Constants.FROM_CONTACT;
import static ch.epfl.sweng.hostme.utils.Constants.IMAGE_PATH;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_USERS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_EMAIL;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FCM_TOKEN;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FIRSTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_LASTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_USER;
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

public class DisplayApartment extends Fragment implements IOnBackPressed {

    private final CollectionReference reference = Database.getCollection(KEY_COLLECTION_USERS);
    private View root;
    private BottomNavigationView bottomNav;
    private String fullAddress;
    private String apartID;
    private Bitmap bitmap;
    private String imagePath;
    private String address;
    private int area;
    private int rent;
    private String lease;
    private String proprietor;
    private String city;
    private int npa;
    private String uid;

    public DisplayApartment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.display_apartment, container, false);

        Button gradeButton = this.root.findViewById(R.id.grade_button);
        gradeButton.setOnClickListener(this::goToGradeFragment);
        Button mapsButton = this.root.findViewById(R.id.maps_button);
        mapsButton.setOnClickListener(this::goToMapsFragment);
        Button streetViewButton = this.root.findViewById(R.id.street_view_button);
        streetViewButton.setOnClickListener(this::goToStreetViewFragment);
        this.bottomNav = this.requireActivity().findViewById(R.id.nav_view);
        this.bottomNav.setVisibility(View.GONE);

        this.unpackBundle();

        Button contactUser = this.root.findViewById(R.id.contact_user_button);
        contactUser.setOnClickListener(view -> chatWithUser(uid));

        changeText(String.valueOf(this.npa), R.id.npa);
        changeText(this.city, R.id.city);
        changeText(this.address, R.id.addr);
        changeText(String.valueOf(this.area), R.id.area);
        changeText(String.valueOf(this.rent), R.id.price);
        changeText(this.lease, R.id.lease);
        changeText(this.proprietor, R.id.proprietor);
        setHorizontalScrollable(this.bitmap, imagePath);

        return this.root;
    }

    private void unpackBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null && !bundle.isEmpty()) {
            this.apartID = bundle.getString(APART_ID);
            this.address = bundle.getString(ADDRESS);
            this.area = bundle.getInt(AREA, 0);
            this.rent = bundle.getInt(RENT, 0);
            this.lease = bundle.getString(LEASE);
            this.proprietor = bundle.getString(PROPRIETOR);
            this.city = bundle.getString(CITY);
            this.npa = bundle.getInt(NPA, 0);
            this.fullAddress = address + " " + city + " " + npa;
            this.uid = bundle.getString(UID);
            this.bitmap = bundle.getParcelable(BITMAP);
            this.imagePath = bundle.getString(IMAGE_PATH);
            bundle.clear();
        }
    }

    /**
     * Set the horizontal scrollable view to have a list of images
     */
    private void setHorizontalScrollable(Bitmap bitmap, String imagePath) {
        LinearLayout gallery = this.root.findViewById(R.id.gallery);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.apart_image, gallery, false);
        ImageView image = view.findViewById(R.id.image_apart);
        image.setImageBitmap(bitmap);
        gallery.addView(view);
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
                        ImageView imageView = newView.findViewById(R.id.image_apart);
                        imageView.setImageBitmap(newBitmap);
                        gallery.addView(newView);
                        loading.setVisibility(View.GONE);
                    });
                } catch (IOException ignored) {
                }
            }
        });
    }

    private FragmentTransaction createTransition(View view, Fragment fragment, String key, String value) {
        Bundle bundle = new Bundle();
        FragmentTransaction fragmentTransaction = ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        bundle.putString(key, value);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
        return fragmentTransaction;
    }

    private void goToStreetViewFragment(View view) {
        FragmentTransaction fragmentTransaction = this.createTransition(view, new StreetViewFragment(), ADDRESS, this.fullAddress);
        fragmentTransaction.commit();
    }

    private void goToMapsFragment(View view) {
        FragmentTransaction fragmentTransaction = this.createTransition(view, new MapsFragment(), ADDRESS, this.fullAddress);
        fragmentTransaction.commit();
    }

    private void goToGradeFragment(View view) {
        FragmentTransaction fragmentTransaction = this.createTransition(view, new GradeApartment(), APART_ID, this.apartID);
        fragmentTransaction.commit();
    }

    /**
     * Launch the activity to chat with the owner of the apartment
     */
    private void chatWithUser(String uid) {
        this.reference.get().addOnSuccessListener(result -> {
            for (DocumentSnapshot doc : result.getDocuments()) {
                if (doc.getId().equals(uid)) {
                    User user = new User(doc.getString(KEY_FIRSTNAME) + " " +
                            doc.getString(KEY_LASTNAME),
                            null, doc.getString(KEY_EMAIL), doc.getString(KEY_FCM_TOKEN), uid);
                    Intent newIntent = new Intent(this.requireActivity().getApplicationContext(), ChatActivity.class);
                    newIntent.putExtra(FROM_CONTACT, apartID);
                    newIntent.putExtra(KEY_USER, user);
                    startActivity(newIntent);
                    this.requireActivity().finish();
                }
            }
        });
    }

    /**
     * change the text view to display the data
     *
     */
    private void changeText(String address, int id) {
        TextView addressText = this.root.findViewById(id);
        addressText.setText(address);
    }

    @Override
    public boolean onBackPressed() {
        bottomNav.setVisibility(View.VISIBLE);
        return false;
    }
}