package ch.epfl.sweng.hostme.ui.search;

import static android.content.Context.MODE_PRIVATE;
import static ch.epfl.sweng.hostme.utils.Constants.ADDR;
import static ch.epfl.sweng.hostme.utils.Constants.APART_ID;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.BITMAP;
import static ch.epfl.sweng.hostme.utils.Constants.BITMAP_FAV;
import static ch.epfl.sweng.hostme.utils.Constants.CITY;
import static ch.epfl.sweng.hostme.utils.Constants.FAVORITES;
import static ch.epfl.sweng.hostme.utils.Constants.FILTERS;
import static ch.epfl.sweng.hostme.utils.Constants.IS_FROM_FILTERS;
import static ch.epfl.sweng.hostme.utils.Constants.NPA;
import static ch.epfl.sweng.hostme.utils.Constants.PREVIEW_1_JPG;
import static ch.epfl.sweng.hostme.utils.Constants.PROPRIETOR;
import static ch.epfl.sweng.hostme.utils.Constants.RENT;
import static ch.epfl.sweng.hostme.utils.Constants.UID;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.utils.Apartment;

public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.ViewHolder> {

    private final CollectionReference reference = Database.getCollection("favorite_apart");
    private List<Apartment> apartments;
    private HashMap<String, Bitmap> hashMap = new HashMap<>();
    private View view;
    private boolean isFavFragment;
    private HashMap<String, Boolean> favMap = new HashMap<>();
    private SharedPreferences preferences;
    private SharedPreferences bitmapPreferences;

    public ApartmentAdapter(List<Apartment> apartments, Context context) {
        this.apartments = apartments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        preferences = view.getContext().getSharedPreferences(FILTERS, Context.MODE_PRIVATE);
        bitmapPreferences = view.getContext().getSharedPreferences(BITMAP_FAV, Context.MODE_PRIVATE);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Apartment apartment = apartments.get(position);
        holder.addr.setText(apartment.getAddress());
        holder.npa.setText(String.valueOf(apartment.getNpa()));
        holder.city.setText(apartment.getCity());
        holder.price.setText(String.format("%s CHF/month", apartment.getRent()));
        holder.area.setText(String.format("%s m\u00B2", apartment.getArea()));
        retrieveAndDisplayImage(holder, apartment, holder.loadingBar);
        holder.itemView.setOnClickListener(view -> displayApartment(apartment, view));
        SharedPreferences pref = holder.itemView.getContext()
                .getSharedPreferences(Auth.getUid() + "Button", MODE_PRIVATE);
        String state = pref.getString(apartment.getDocID() + "pressed", "no");
        if (isFavFragment) {
            holder.favouriteButton.setChecked(true);
        } else {
            holder.favouriteButton.setChecked(state.equals("yes"));
        }
        holder.favouriteButton.setOnCheckedChangeListener((compoundButton, b) -> {
            compoundButton.startAnimation(createToggleAnimation());
            updateApartDB(holder.itemView.getContext(), apartment, compoundButton.isChecked(), isFavFragment);
        });
    }


    /**
     * Save a favourite apartment in the database
     */
    private void updateApartDB(Context context, Apartment apartment,
                               boolean isAdded, boolean isFavFragment) {
        String uid = Auth.getUid();
        DocumentReference documentRef = reference.document(uid);
        setPreferences(context, isFavFragment);
        SharedPreferences.Editor editor = context.
                getSharedPreferences(Auth.getUid() + "Button", MODE_PRIVATE).edit();
        if (!preferences.getBoolean(IS_FROM_FILTERS, true)) {
            if (isAdded) {
                favMap.put(apartment.getDocID(), true);
                editor.putString(apartment.getDocID() + "pressed", "yes");
                editor.apply();
                documentRef.get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                documentRef.update(FAVORITES, FieldValue.arrayUnion(apartment.getDocID()));
                            } else {
                                Map<String, ArrayList> mapData = new HashMap<>();
                                ArrayList<String> favorites = new ArrayList<>();
                                favorites.add(apartment.getDocID());
                                mapData.put(FAVORITES, favorites);
                                documentRef.set(mapData);
                            }
                            Toast.makeText(view.getContext(), "Apartment added to your favorites",
                                    Toast.LENGTH_SHORT).show();
                        });
            } else {
                favMap.put(apartment.getDocID(), false);
                editor.putString(apartment.getDocID() + "pressed", "no");
                editor.apply();
                documentRef.get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                documentRef.update(FAVORITES, FieldValue.arrayRemove(apartment.getDocID()));
                            }
                        });
                Toast.makeText(view.getContext(), "Apartment removed from your favorites",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Change the preference to know if we are in the favorite fragment or not,
     * if we are we will load the data but if we are in the main recyclerview we will not
     *
     * @param context
     * @param isFavFragment
     */
    private void setPreferences(Context context, boolean isFavFragment) {
        SharedPreferences prefFragment = context.getSharedPreferences("FavoriteFragment", MODE_PRIVATE);
        if (isFavFragment) {
            SharedPreferences.Editor editor1 = prefFragment.edit();
            editor1.putBoolean("isFavorite", true);
            editor1.apply();
        } else {
            SharedPreferences.Editor editor2 = prefFragment.edit();
            editor2.putBoolean("isFavorite", false);
            editor2.apply();
        }
    }

    /**
     * Create animation for the Toggle button
     */
    private ScaleAnimation createToggleAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        return scaleAnimation;
    }

    /**
     * Launch the fragment that displays the specific data for apartment
     *
     * @param apartment
     * @param view
     */
    private void displayApartment(Apartment apartment, View view) {
        Bundle bundle = new Bundle();
        Fragment fragment = new DisplayApartment();
        FragmentTransaction fragmentTransaction =
                ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        bundle.putString(APART_ID, apartment.getDocID());
        bundle.putString(UID, apartment.getUid());
        bundle.putString(ADDR, apartment.getAddress());
        bundle.putInt(NPA, apartment.getNpa());
        bundle.putString(CITY, apartment.getCity());
        bundle.putInt(RENT, apartment.getRent());
        bundle.putInt(AREA, apartment.getArea());
        bundle.putString(PROPRIETOR, apartment.getProprietor());
        bundle.putParcelable(BITMAP, hashMap.get(apartment.getDocID()));
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        return apartments.size();
    }

    /**
     * Retrieve image from Firestore storage and display it
     *
     * @param holder
     * @param model
     * @param loadingBar
     */
    public void retrieveAndDisplayImage(@NonNull ViewHolder holder, @NonNull Apartment model, ProgressBar loadingBar) {
        loadingBar.setVisibility(View.VISIBLE);
        StorageReference storageReference = Storage.getStorageReferenceByChild(model.getImagePath() + PREVIEW_1_JPG);
        if (!bitmapPreferences.getString(model.getDocID(), "").equals("")) {
            String encodedImage = bitmapPreferences.getString(model.getDocID(), "");
            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);
            holder.image.setImageBitmap(bitmapImage);
            System.out.println("c'est fait");
            loadingBar.setVisibility(View.GONE);
            hashMap.put(model.getDocID(), bitmapImage);
        } else {
            try {
                final File localFile = File.createTempFile("preview1", "jpg");
                storageReference.getFile(localFile)
                        .addOnSuccessListener(result -> {
                            loadingBar.setVisibility(View.GONE);
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            if (isFavFragment) {
                                saveBitmap(model, bitmap);
                            }
                            model.setBitmap(bitmap);
                            hashMap.put(model.getDocID(), bitmap);
                            holder.image.setImageBitmap(bitmap);
                        });
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Save the bitmap in shared preferences for caching the favorites apartments
     *
     * @param model
     * @param bitmap
     */
    private void saveBitmap(@NonNull Apartment model, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] compressImage = baos.toByteArray();
        String sEncodedImage = Base64.encodeToString(compressImage, Base64.DEFAULT);
        bitmapPreferences.edit().putString(model.getDocID(), sEncodedImage).apply();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Set the apartments list
     *
     * @param apartments
     */
    public void setApartments(List<Apartment> apartments) {
        this.apartments = apartments;
        notifyDataSetChanged();
    }

    /**
     * hide the favorite button if you are in favorite fragment
     */
    public void setFavFragment() {
        this.isFavFragment = true;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView price;
        public TextView addr;
        public TextView area;
        public TextView city;
        public TextView npa;
        public ImageView image;
        public CardView cardView;
        public ToggleButton favouriteButton;
        public ProgressBar loadingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            this.price = itemView.findViewById(R.id.list_price);
            this.addr = itemView.findViewById(R.id.list_addr);
            this.area = itemView.findViewById(R.id.list_area);
            this.npa = itemView.findViewById(R.id.list_npa);
            this.city = itemView.findViewById(R.id.list_city);
            this.image = itemView.findViewById(R.id.apartment_image);
            this.favouriteButton = itemView.findViewById(R.id.button_favourite);
            this.loadingBar = itemView.findViewById(R.id.loading_bar);
            cardView = itemView.findViewById(R.id.cardView);
        }

    }
}
