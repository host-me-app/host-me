package ch.epfl.sweng.hostme.ui.search;

import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS_PATH;
import static ch.epfl.sweng.hostme.utils.Constants.CITY;
import static ch.epfl.sweng.hostme.utils.Constants.NPA;
import static ch.epfl.sweng.hostme.utils.Constants.PROPRIETOR;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.utils.Apartment;

public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.ViewHolder> {

    public static final String BITMAP = "bitmap";
    private List<Apartment> apartments;
    private Bitmap bitmap;
    public static final String UID = "uid";
    public static final String ADDR = "addr";
    public static final String RENT = "rent";
    public static final String AREA = "area";
    public static final String PREVIEW_1_JPG = "/preview1.jpg";
    public static final String LID = "lid";


    public ApartmentAdapter(List<Apartment> apartments) {
        this.apartments = apartments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Apartment apartment = apartments.get(position);
        holder.addr.setText(apartment.getAddress());
        holder.npa.setText(String.valueOf(apartment.getNpa()));
        holder.city.setText(apartment.getCity());
        holder.price.setText(String.format("%s CHF/month", apartment.getRent()));
        holder.area.setText(String.format("%s m²", apartment.getArea()));
        retrieveAndDisplayImage(holder, apartment);
        holder.itemView.setOnClickListener(view -> {
            displayApartment(apartment, view);
        });
        holder.favouriteButton.setOnCheckedChangeListener((compoundButton, b) -> {
            compoundButton.startAnimation(createToggleAnimation());
        });
        holder.favouriteButton.setOnClickListener(view -> {
            saveApartToDB();
        });
    }

    /**
     * Create animation for the Tuggle button
     */
    private ScaleAnimation createToggleAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        return scaleAnimation;
    }


    /**
     * Save a fourite apartment in the database
     */
    private void saveApartToDB() {
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
        bundle.putString(UID, apartment.getUid());
        bundle.putString(ADDR, apartment.getAddress());
        bundle.putInt(NPA, apartment.getNpa());
        bundle.putString(CITY, apartment.getCity());
        bundle.putInt(RENT, apartment.getRent());
        bundle.putInt(AREA, apartment.getArea());
        bundle.putString(LID, apartment.getLid());
        bundle.putString(PROPRIETOR, apartment.getProprietor());
        bundle.putParcelable(BITMAP, bitmap);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        return apartments.size();
    }

    public void retrieveAndDisplayImage(@NonNull ViewHolder holder, @NonNull Apartment model) {
        StorageReference storageReference = Storage.getStorageReferenceByChild(APARTMENTS_PATH + model.getLid() + PREVIEW_1_JPG);
        try {
            final File localFile = File.createTempFile("preview1", "jpg");
            storageReference.getFile(localFile)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            holder.image.setImageBitmap(bitmap);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setApartments(List<Apartment> apartments) {
        this.apartments = apartments;
        notifyDataSetChanged();
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

        public ViewHolder(View itemView) {
            super(itemView);
            this.price = itemView.findViewById(R.id.list_price);
            this.addr = itemView.findViewById(R.id.list_addr);
            this.area = itemView.findViewById(R.id.list_area);
            this.npa = itemView.findViewById(R.id.list_npa);
            this.city = itemView.findViewById(R.id.list_city);
            this.image = itemView.findViewById(R.id.apartment_image);
            this.favouriteButton = itemView.findViewById(R.id.button_favourite);
            cardView = itemView.findViewById(R.id.cardView);
        }

    }
}
