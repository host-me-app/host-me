package ch.epfl.sweng.hostme.adapter;

import static ch.epfl.sweng.hostme.utils.Constants.ADDR;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.CITY;
import static ch.epfl.sweng.hostme.utils.Constants.LEASE;
import static ch.epfl.sweng.hostme.utils.Constants.PATH;
import static ch.epfl.sweng.hostme.utils.Constants.NPA;
import static ch.epfl.sweng.hostme.utils.Constants.BEDS;
import static ch.epfl.sweng.hostme.utils.Constants.PREVIEW_1_JPG;
import static ch.epfl.sweng.hostme.utils.Constants.PROPRIETOR;
import static ch.epfl.sweng.hostme.utils.Constants.RENT;
import static ch.epfl.sweng.hostme.utils.Constants.UID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ch.epfl.sweng.hostme.Apartment;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.ui.search.DisplayApartment;

public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.ViewHolder> {

    private List<Apartment> apartments;


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
            Intent intent = new Intent(holder.itemView.getContext(), DisplayApartment.class);
            intent.putExtra(UID, apartment.getUid());
            intent.putExtra(ADDR, apartment.getAddress());
            intent.putExtra(NPA, apartment.getNpa());
            intent.putExtra(CITY, apartment.getCity());
            intent.putExtra(RENT, apartment.getRent());
            intent.putExtra(AREA, apartment.getArea());
            intent.putExtra(PATH, apartment.getImagePath());
            intent.putExtra(LEASE, apartment.getCurrentLease());
            intent.putExtra(BEDS, apartment.getBeds());
            intent.putExtra(PROPRIETOR, apartment.getProprietor());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return apartments.size();
    }

    public void retrieveAndDisplayImage(@NonNull ViewHolder holder, @NonNull Apartment model) {
        StorageReference storageReference = Storage.getStorageReferenceByChild(model.getImagePath() + PREVIEW_1_JPG);
        try {
            final File localFile = File.createTempFile("preview1", "jpg");
            storageReference.getFile(localFile)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
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
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView price;
        public TextView addr;
        public TextView area;
        public TextView city;
        public TextView npa;
        public ImageView image;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.price = itemView.findViewById(R.id.list_price);
            this.addr = itemView.findViewById(R.id.list_addr);
            this.area = itemView.findViewById(R.id.list_area);
            this.npa = itemView.findViewById(R.id.list_npa);
            this.city = itemView.findViewById(R.id.list_city);
            this.image = itemView.findViewById(R.id.apartment_image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
