/*
package ch.epfl.sweng.hostme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private ArrayList<Apartment> apartmentList;

    // RecyclerView recyclerView;
    public MyListAdapter(ArrayList<Apartment> apartmentList) {
        this.apartmentList = apartmentList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Apartment apartment = apartmentList.get(position);
        holder.textView.setText(apartment.getRent());
        holder.relativeLayout.setOnClickListener(view ->
                Toast.makeText(view.getContext(),"click on item: "+ apartment.getRent(),
                        Toast.LENGTH_LONG).show());
    }


    @Override
    public int getItemCount() {
        return apartmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.list_price);
            this.textView = itemView.findViewById(R.id.list_name);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
*/
