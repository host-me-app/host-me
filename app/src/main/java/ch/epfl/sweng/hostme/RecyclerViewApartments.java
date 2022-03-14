package ch.epfl.sweng.hostme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RecyclerviewApartments extends RecyclerView.Adapter<RecyclerviewApartments.MyViewHolder> {

    private List<Apartment> apartmentList;

    public RecyclerviewApartments(List<Apartment> apartments) {
        this.apartmentList = apartments;
    }


    @Override
    public RecyclerviewApartments.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apartments_row, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerviewApartments.MyViewHolder holder, final int position) {
        final Apartment apartment = apartmentList.get(position);
        holder.name.setText(apartment.getName());
        holder.price.setText(String.valueOf(apartment.getRent()));
    }

    @Override
    public int getItemCount() {
        return apartmentList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView name,price;
        private LinearLayout itemLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            price = itemView.findViewById(R.id.tvPrice);
            itemLayout =  itemView.findViewById(R.id.itemLayout);
        }
    }
}
