package com.archidni.archidni.Ui.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.archidni.archidni.Model.Places.Parking;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ViewHolder> {

    private ArrayList<Parking> parkings;
    private OnItemClickListener onItemClickListener;

    public ParkingAdapter(ArrayList<Parking> parkings, OnItemClickListener onItemClickListener) {
        this.parkings = parkings;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parking ,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(parkings.get(position));
            }
        });
        Parking parking = parkings.get(position);
        holder.nameText.setText(parking.getMainText());
        holder.capacityText.setText("Capacité : "+parking.getCapacity());
    }

    @Override
    public int getItemCount() {
        return parkings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.container)
        View container;
        @BindView(R.id.text_name)
        TextView nameText;
        @BindView(R.id.text_capacity)
        TextView capacityText;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Parking parking);
    }

}
