package ch.epfl.sweng.hostme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RecyclerViewApartments extends RecyclerView.Adapter<RecyclerViewApartments.MyViewHolder> {

    private static List<Apartment> apartmentList;
    private static LayoutInflater mInflater;
    private static MyViewHolder.ItemClickListener mClickListener;

    RecyclerViewApartments(Context context, List<Apartment> apartments) {
        mInflater = LayoutInflater.from(context);
        apartmentList = apartments;
    }


    @Override
    public RecyclerViewApartments.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.apartments_row, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewApartments.MyViewHolder holder, final int position) {
        Apartment apartment = apartmentList.get(position);
        holder.price.setText(String.valueOf(apartment.getRent()));
    }

    @Override
    public int getItemCount() {
        return apartmentList.size();
    }

    public Apartment getApart(int id) {
        return apartmentList.get(id);
    }

    void setClickListener(MyViewHolder.ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView price;

        public MyViewHolder(View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getBindingAdapterPosition());
        }

        public interface ItemClickListener {
            void onItemClick(View view, int position);
        }
    }
}