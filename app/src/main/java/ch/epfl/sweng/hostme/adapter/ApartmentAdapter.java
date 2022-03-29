package ch.epfl.sweng.hostme.adapter;

import static ch.epfl.sweng.hostme.utils.Constants.ADDR;
import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS_PATH;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.LEASE;
import static ch.epfl.sweng.hostme.utils.Constants.LID;
import static ch.epfl.sweng.hostme.utils.Constants.OCCUPANT;
import static ch.epfl.sweng.hostme.utils.Constants.PREVIEW_1_JPG;
import static ch.epfl.sweng.hostme.utils.Constants.PROPRIETOR;
import static ch.epfl.sweng.hostme.utils.Constants.RENT;

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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ch.epfl.sweng.hostme.Apartment;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.ui.search.DisplayApartment;

public class ApartmentAdapter extends FirestoreRecyclerAdapter<Apartment, ApartmentAdapter.ViewHolder> {

    private StorageReference storageReference;


    public ApartmentAdapter(@NonNull FirestoreRecyclerOptions<Apartment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Apartment model) {
        holder.addr.setText(model.getAddress());
        holder.price.setText(String.format("%s CHF/month", model.getRent()));
        holder.area.setText(String.format("%s m²", model.getArea()));
        retrieveAndDisplayImage(holder, model);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), DisplayApartment.class);
            intent.putExtra(ADDR, model.getAddress());
            intent.putExtra(RENT, model.getRent());
            intent.putExtra(AREA, model.getArea());
            intent.putExtra(LID, model.getLid());
            //String lease = DateFormat.format("dd-MM-yyyy", model.getCurrentLease()).toString();
            intent.putExtra(LEASE, model.getCurrentLease());
            intent.putExtra(OCCUPANT, model.getOccupants());
            intent.putExtra(PROPRIETOR, model.getProprietor());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    public void retrieveAndDisplayImage(@NonNull ViewHolder holder, @NonNull Apartment model) {
        storageReference = FirebaseStorage.getInstance().getReference().child(APARTMENTS_PATH + model.getLid() + PREVIEW_1_JPG);
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

    public void setApartments(List<Apartment> apartments) {
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView price;
        public TextView addr;
        public TextView area;
        public ImageView image;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.price = itemView.findViewById(R.id.list_price);
            this.addr = itemView.findViewById(R.id.list_addr);
            this.area = itemView.findViewById(R.id.list_area);
            this.image = itemView.findViewById(R.id.apartment_image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
