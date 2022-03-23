package ch.epfl.sweng.hostme.ui.search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import ch.epfl.sweng.hostme.Apartment;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private final static long NB_APARTMENT_TO_DISPLAY = 10;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter recyclerAdapter;
    private StorageReference storageReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.recycler_view, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = root.findViewById(R.id.recyclerView);
        Query query = firebaseFirestore.collection("apartments").limit(NB_APARTMENT_TO_DISPLAY);
        FirestoreRecyclerOptions<Apartment> options = new FirestoreRecyclerOptions.Builder<Apartment>()
                .setQuery(query, Apartment.class)
                .setLifecycleOwner(this)
                .build();

        recyclerAdapter = new FirestoreRecyclerAdapter<Apartment, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Apartment model) {
                holder.addr.setText(model.getAddress());
                holder.price.setText(String.valueOf(model.getRent()));
                holder.area.setText(String.valueOf(model.getArea()));
                retrieveAndDisplayImage(holder, model, root);
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                return new ViewHolder(view);
            }

        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerAdapter);

        return root;
    }

    private void retrieveAndDisplayImage(@NonNull ViewHolder holder, @NonNull Apartment model, View root) {
        storageReference = FirebaseStorage.getInstance().getReference().child("apartments/" + model.getLid() + "/preview1.jpg");
        try {
            final File localFile = File.createTempFile("preview1", "jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(e -> {
                        Toast.makeText(root.getContext(), "Display ! ", Toast.LENGTH_SHORT).show();
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        holder.image.setImageBitmap(bitmap);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(root.getContext(), "Can't DIsplay ! ", Toast.LENGTH_SHORT).show());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView price;
        public TextView addr;
        public TextView area;
        public ImageView image;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.price = itemView.findViewById(R.id.list_price);
            this.addr = itemView.findViewById(R.id.list_addr);
            this.area = itemView.findViewById(R.id.list_area);
            this.image = itemView.findViewById(R.id.apartment_image);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
