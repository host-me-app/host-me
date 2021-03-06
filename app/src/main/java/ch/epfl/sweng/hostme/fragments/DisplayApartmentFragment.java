package ch.epfl.sweng.hostme.fragments;

import static ch.epfl.sweng.hostme.utils.Constants.ADDRESS;
import static ch.epfl.sweng.hostme.utils.Constants.APART_ID;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.BATH;
import static ch.epfl.sweng.hostme.utils.Constants.BEDS;
import static ch.epfl.sweng.hostme.utils.Constants.BITMAP;
import static ch.epfl.sweng.hostme.utils.Constants.CITY;
import static ch.epfl.sweng.hostme.utils.Constants.DEPOSIT;
import static ch.epfl.sweng.hostme.utils.Constants.FROM;
import static ch.epfl.sweng.hostme.utils.Constants.FROM_CONTACT;
import static ch.epfl.sweng.hostme.utils.Constants.IMAGE_PATH;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_USERS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_EMAIL;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FCM_TOKEN;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FIRSTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_LASTNAME;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_USER;
import static ch.epfl.sweng.hostme.utils.Constants.KITCHEN;
import static ch.epfl.sweng.hostme.utils.Constants.NPA;
import static ch.epfl.sweng.hostme.utils.Constants.PETS;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.activities.ChatActivity;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.users.User;
import ch.epfl.sweng.hostme.utils.IOnBackPressed;

public class DisplayApartmentFragment extends Fragment implements IOnBackPressed {

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
    private int beds;
    private String proprietor;
    private String city;
    private int npa;
    private String uid;
    private String bath;
    private String kitchen;
    private int deposit;
    private boolean pets;

    public DisplayApartmentFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.display_apartment, container, false);

        Button gradeButton = this.root.findViewById(R.id.grade_button);
        gradeButton.setOnClickListener(v -> this.goToGradeFragment());
        Button mapsButton = this.root.findViewById(R.id.maps_button);
        mapsButton.setOnClickListener(v -> this.goToMapsFragment());
        Button streetViewButton = this.root.findViewById(R.id.street_view_button);
        streetViewButton.setOnClickListener(v -> this.goToStreetViewFragment());
        this.bottomNav = this.requireActivity().findViewById(R.id.nav_view);
        this.bottomNav.setVisibility(View.GONE);

        this.unpackBundle();

        Button contactUser = root.findViewById(R.id.contact_user_button);
        contactUser.setOnClickListener(view -> chatWithUser(uid));
        changeText(String.valueOf(npa), R.id.npa);
        changeText(String.valueOf(beds), R.id.beds);
        changeText(city, R.id.city);
        changeText(address, R.id.addr);
        changeText(String.valueOf(area), R.id.area);
        changeText(String.valueOf(rent), R.id.price);
        changeText(proprietor, R.id.proprietor);
        changeText(kitchen, R.id.kitchen);
        changeText(bath, R.id.bathroom);
        changeText(String.valueOf(deposit), R.id.deposit);
        setHorizontalScrollable(bitmap, imagePath);
        if (!pets) changeText("not", R.id.pets);
        return this.root;
    }

    /**
     * get the fields from the bundle
     */
    private void unpackBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null && !bundle.isEmpty()) {
            apartID = bundle.getString(APART_ID);
            address = bundle.getString(ADDRESS);
            area = bundle.getInt(AREA, 0);
            rent = bundle.getInt(RENT, 0);
            proprietor = bundle.getString(PROPRIETOR);
            city = bundle.getString(CITY);
            beds = bundle.getInt(BEDS);
            npa = bundle.getInt(NPA, 0);
            fullAddress = address + " " + city + " " + npa;
            uid = bundle.getString(UID);
            bitmap = bundle.getParcelable(BITMAP);
            imagePath = bundle.getString(IMAGE_PATH);
            bath = bundle.getString(BATH);
            kitchen = bundle.getString(KITCHEN);
            deposit = bundle.getInt(DEPOSIT);
            pets = bundle.getBoolean(PETS);
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

    /**
     * create transaction for fragments
     *
     * @param fragment to change
     * @param key      update the bundle
     * @param value    update the bundle with corresponding key
     * @return
     */
    private FragmentTransaction createTransaction(Fragment fragment, String key, String value) {
        Bundle bundle = new Bundle();
        FragmentTransaction fragmentTransaction = this.requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        bundle.putString(key, value);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_container, fragment);
        return fragmentTransaction;
    }

    /**
     * go to the street view fragment
     */
    private void goToStreetViewFragment() {
        FragmentTransaction fragmentTransaction = this.createTransaction(new StreetViewFragment(), ADDRESS, this.fullAddress);
        fragmentTransaction.commit();
    }

    /**
     * go to the maps fragment
     */
    private void goToMapsFragment() {
        FragmentTransaction fragmentTransaction = this.createTransaction(new MapsFragment(), ADDRESS, this.fullAddress);
        fragmentTransaction.commit();
    }

    /**
     * go to the grade fragment
     */
    private void goToGradeFragment() {
        FragmentTransaction fragmentTransaction = this.createTransaction(new GradeApartment(), APART_ID, this.apartID);
        fragmentTransaction.commit();
    }

    /**
     * launch the activity to chat with the owner of the apartment
     */
    private void chatWithUser(String uid) {
        this.reference.get().addOnSuccessListener(result -> {
            for (DocumentSnapshot doc : result.getDocuments()) {
                if (doc.getId().equals(uid)) {
                    User user = new User(doc.getString(KEY_FIRSTNAME) + " " +
                            doc.getString(KEY_LASTNAME),
                            null, doc.getString(KEY_EMAIL), doc.getString(KEY_FCM_TOKEN), uid);
                    Intent newIntent = new Intent(this.requireActivity().getApplicationContext(), ChatActivity.class);
                    newIntent.putExtra(FROM, apartID);
                    newIntent.putExtra(KEY_USER, user);
                    newIntent.putExtra(FROM_CONTACT, true);
                    startActivity(newIntent);
                    this.requireActivity().finish();
                }
            }
        });
    }

    /**
     * change the text view to display the data
     */
    private void changeText(String field, int id) {
        TextView fieldText = root.findViewById(id);
        fieldText.setText(field);
    }

    @Override
    public boolean onBackPressed() {
        this.bottomNav.setVisibility(View.VISIBLE);
        return false;
    }
}